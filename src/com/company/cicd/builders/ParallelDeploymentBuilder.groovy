package com.company.cicd.builders

import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.context.DeploymentContext
import com.company.cicd.managers.ApprovalManager
import com.company.cicd.managers.DeploymentManager
import com.company.cicd.types.EnvironmentConfig

class ParallelDeploymentBuilder implements Serializable {
  private final def steps
  private final DeploymentManager deploymentManager
  private final ApprovalManager approvalManager
  private final PipelineConfiguration configuration

  ParallelDeploymentBuilder(def steps, DeploymentManager deploymentManager, ApprovalManager approvalManager, PipelineConfiguration configuration) {
    this.steps = steps
    this.deploymentManager = deploymentManager
    this.approvalManager = approvalManager
    this.configuration = configuration
  }

  Map<String, Closure> build(List<EnvironmentConfig> environments) {
    Map<String, Closure> branches = [:]
    environments.each { EnvironmentConfig envConfig ->
      branches[envConfig.stageName()] = {
        steps.stage(envConfig.stageName()) {
          steps.timeout(time: envConfig.timeoutMinutes, unit: 'MINUTES') {
            steps.retry(envConfig.retries) {
              approvalManager.approveIfRequired(envConfig)
              deploymentManager.deploy(new DeploymentContext(steps, configuration, envConfig))
            }
          }
        }
      }
    }
    return branches
  }
}
