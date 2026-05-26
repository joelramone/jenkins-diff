package com.company.cicd.managers

class S3Comparator implements Serializable {
  private final def steps

  S3Comparator(def steps) { this.steps = steps }

  void syncFromS3(String sourceUri) {
    steps.sh 'mkdir -p .s3-sync'
    steps.sh "aws s3 sync ${sourceUri} .s3-sync"
  }

  void copyToWorkspace() {
    steps.sh 'rsync -av --checksum --delete .s3-sync/ ./'
  }

  String checksumDelta() {
    steps.sh(script: "git diff --name-only", returnStdout: true).trim()
  }
}
