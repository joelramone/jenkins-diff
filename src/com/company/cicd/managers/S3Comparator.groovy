package com.company.cicd.managers

class S3Comparator implements Serializable {
  private final def steps

  S3Comparator(def steps) { this.steps = steps }

  void syncFromS3(String sourceUri, String awsRegion) {
    steps.sh 'mkdir -p .s3-sync'
    steps.sh "AWS_DEFAULT_REGION=${awsRegion} aws s3 sync ${sourceUri} .s3-sync --delete"
  }

  void copyToWorkspace() {
    steps.sh 'rsync -a --checksum --delete --exclude .git --exclude .s3-sync .s3-sync/ ./'
  }
}
