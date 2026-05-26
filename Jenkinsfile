@Library('nrtc-shared-library@main') _

pipeline {
  agent any

  options {
    timestamps()
    disableConcurrentBuilds()
    ansiColor('xterm')
  }

  parameters {
    string(name: 'RELEASE_VERSION', defaultValue: '', description: 'Release version')
    booleanParam(name: 'DRY_RUN', defaultValue: false, description: 'Dry run')
  }

  stages {
    stage('Enterprise Pipeline') {
      steps {
        script {
          nrtcEnterprisePipeline(
            releaseVersion: params.RELEASE_VERSION,
            dryRun: params.DRY_RUN
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
