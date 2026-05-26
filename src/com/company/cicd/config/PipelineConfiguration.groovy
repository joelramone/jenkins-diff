package com.company.cicd.config

class PipelineConfiguration implements Serializable {
  String branchName
  String gitUrl
  String releaseVersion
  boolean dryRun
  String s3SourceUri
  String releaseBranchRegex
  String gitUserName
  String gitUserEmail
  String awsRegion
  int defaultRetries
  int defaultTimeout
  boolean globalFailFast

  static PipelineConfiguration fromRuntime(def steps, Map args = [:]) {
    Map pipelineYaml = steps.readYaml(file: 'pipeline-config/pipeline.yaml') ?: [:]
    Map global = (Map) (pipelineYaml.global ?: [:])

    PipelineConfiguration cfg = new PipelineConfiguration()
    cfg.branchName = steps.env.BRANCH_NAME ?: ''
    cfg.gitUrl = steps.env.GIT_URL ?: ''
    cfg.releaseVersion = (args.releaseVersion ?: '').toString().trim()
    cfg.dryRun = (args.dryRun ?: false) as boolean
    cfg.s3SourceUri = global.s3?.sourceUri ?: ''
    cfg.releaseBranchRegex = global.releases?.branchRegex ?: '^nrtc-[0-9]+\\.[0-9]+\\.[0-9]+$'
    cfg.gitUserName = global.git?.userName ?: 'jenkins-bot'
    cfg.gitUserEmail = global.git?.userEmail ?: 'jenkins-bot@company.com'
    cfg.awsRegion = global.aws?.region ?: 'us-east-1'
    cfg.defaultRetries = (global.defaults?.retries ?: 1) as int
    cfg.defaultTimeout = (global.defaults?.timeout ?: 20) as int
    cfg.globalFailFast = (global.defaults?.failFast ?: false) as boolean
    return cfg
  }
}
