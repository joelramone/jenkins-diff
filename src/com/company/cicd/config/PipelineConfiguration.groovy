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
  boolean globalFailFast = false

  static PipelineConfiguration fromArgs(Map args) {
    PipelineConfiguration cfg = new PipelineConfiguration()
    cfg.branchName = args.branchName ?: ''
    cfg.gitUrl = args.gitUrl ?: ''
    cfg.releaseVersion = args.releaseVersion ?: ''
    cfg.dryRun = args.dryRun ?: false
    cfg.s3SourceUri = args.s3SourceUri ?: ''
    cfg.releaseBranchRegex = args.releaseBranchRegex ?: '^nrtc-[0-9]+\\.[0-9]+\\.[0-9]+$'
    cfg.gitUserName = args.gitUserName ?: 'jenkins-bot'
    cfg.gitUserEmail = args.gitUserEmail ?: 'jenkins-bot@company.com'
    return cfg
  }
}
