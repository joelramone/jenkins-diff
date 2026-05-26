@Library('nrtc-shared-library@main') _

pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
    ansiColor('xterm')
  }

  parameters {
    string(name: 'RELEASE_VERSION', defaultValue: '', description: 'Release version in format x.y.z (optional for main/master branch)')
    booleanParam(name: 'DRY_RUN', defaultValue: false, description: 'Run without push/deploy changes')
  }

  environment {
    AWS_DEFAULT_REGION = 'us-east-1'
    S3_SOURCE_URI = 's3://nrtc-releases/latest'
    GIT_USER_NAME = 'jenkins-bot'
    GIT_USER_EMAIL = 'jenkins-bot@company.com'
    RELEASE_BRANCH_REGEX = '^nrtc-[0-9]+\\.[0-9]+\\.[0-9]+$'
  }

  stages {
    stage('Enterprise CI/CD Orchestration') {
      steps {
        script {
          nrtcEnterprisePipeline(
            branchName: env.BRANCH_NAME,
            gitUrl: env.GIT_URL,
            releaseVersion: params.RELEASE_VERSION,
            dryRun: params.DRY_RUN,
            s3SourceUri: env.S3_SOURCE_URI,
            releaseBranchRegex: env.RELEASE_BRANCH_REGEX,
            gitUserName: env.GIT_USER_NAME,
            gitUserEmail: env.GIT_USER_EMAIL
          )
        }
      }
    }
  }

  post {
    always {
      cleanWs(deleteDirs: true)
    }
  }
}
