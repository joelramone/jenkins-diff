package com.company.cicd.orchestrator

import com.company.cicd.adapters.LegacyApprovalAdapter
import com.company.cicd.adapters.LegacyDeploymentAdapter
import com.company.cicd.builders.ParallelDeploymentBuilder
import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.context.DeploymentContext
import com.company.cicd.factory.DeploymentFactory
import com.company.cicd.managers.ApprovalManager
import com.company.cicd.managers.BranchManager
import com.company.cicd.managers.DeploymentManager
import com.company.cicd.managers.GitManager
import com.company.cicd.managers.NotificationManager
import com.company.cicd.managers.S3Comparator
import com.company.cicd.registry.EnvironmentRegistry
import com.company.cicd.types.EnvironmentConfig

class PipelineOrchestrator implements Serializable {
  private final def steps
  private final PipelineConfiguration configuration
  private final EnvironmentRegistry environmentRegistry
  private final GitManager gitManager
  private final BranchManager branchManager
  private final S3Comparator s3Comparator
  private final DeploymentManager deploymentManager
  private final ApprovalManager approvalManager
  private final NotificationManager notificationManager
  private final LegacyDeploymentAdapter legacyDeploymentAdapter

  PipelineOrchestrator(def steps, PipelineConfiguration configuration) {
    this.steps = steps
    this.configuration = configuration
    this.environmentRegistry = new EnvironmentRegistry(steps, configuration)
    this.gitManager = new GitManager(steps)
    this.branchManager = new BranchManager(steps)
    this.s3Comparator = new S3Comparator(steps)
    this.deploymentManager = new DeploymentManager(new DeploymentFactory())
    this.approvalManager = new ApprovalManager(new LegacyApprovalAdapter(steps))
    this.notificationManager = new NotificationManager(steps)
    this.legacyDeploymentAdapter = new LegacyDeploymentAdapter(steps)
  }

  void execute() {
    steps.stage('Sync S3 and Detect Changes') {
      gitManager.configureIdentity(configuration.gitUserName, configuration.gitUserEmail)
      s3Comparator.syncFromS3(configuration.s3SourceUri, configuration.awsRegion)
      s3Comparator.copyToWorkspace()
      String releaseBranch = resolveReleaseBranch()
      boolean existsRemote = branchManager.remoteBranchExists(releaseBranch)
      branchManager.createOrSwitch(releaseBranch, existsRemote)

      if (gitManager.hasChanges()) {
        String diff = gitManager.diffSummary()
        gitManager.commitTrackedChanges("chore(release): sync artifacts for ${releaseBranch}")
        if (!configuration.dryRun) {
          gitManager.push(releaseBranch)
        }
        notificationManager.traceability("Changes committed for ${releaseBranch}: ${diff}")
      } else {
        notificationManager.traceability("No changes detected for ${releaseBranch}")
      }
    }

    if (branchManager.isReleaseBranch(configuration.branchName, configuration.releaseBranchRegex)) {
      steps.stage('Legacy Integration Hooks') {
        legacyDeploymentAdapter.runLegacyIntegration()
        legacyDeploymentAdapter.runLegacyApplyDeployment()
      }
      runDeployments()
    } else {
      notificationManager.traceability("Branch ${configuration.branchName} is not release; deployment skipped")
    }
  }

  private void runDeployments() {
    ParallelDeploymentBuilder builder = new ParallelDeploymentBuilder(steps, deploymentManager, approvalManager, configuration)

    steps.stage('Deploy Lower Environments') {
      steps.parallel(builder.build(environmentRegistry.byGroup('lower')), failFast: configuration.globalFailFast)
    }

    steps.stage('Deploy Upper Environments') {
      steps.parallel(builder.build(environmentRegistry.byGroup('upper')), failFast: configuration.globalFailFast)
    }

    steps.stage('Deploy Production') {
      EnvironmentConfig prod = environmentRegistry.production()
      steps.timeout(time: prod.timeoutMinutes, unit: 'MINUTES') {
        steps.retry(prod.retries) {
          approvalManager.approveIfRequired(prod)
          deploymentManager.deploy(new DeploymentContext(steps, configuration, prod))
        }
      }
    }
  }

  private String resolveReleaseBranch() {
    if (configuration.releaseVersion?.trim()) {
      return "nrtc-${configuration.releaseVersion.trim()}"
    }
    if (branchManager.isReleaseBranch(configuration.branchName, configuration.releaseBranchRegex)) {
      return configuration.branchName
    }
    throw new IllegalArgumentException('RELEASE_VERSION is required on non-release branches')
  }
}
