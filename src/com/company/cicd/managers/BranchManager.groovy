package com.company.cicd.managers

class BranchManager implements Serializable {
  private final def steps

  BranchManager(def steps) { this.steps = steps }

  boolean isReleaseBranch(String branchName, String regexPattern) {
    return branchName ==~ regexPattern
  }

  boolean remoteBranchExists(String branchName) {
    String output = steps.sh(script: "git ls-remote --heads origin ${branchName}", returnStdout: true).trim()
    return !output.isEmpty()
  }

  void createOrSwitch(String branchName, boolean existsRemotely) {
    if (existsRemotely) {
      steps.sh "git checkout ${branchName}"
      steps.sh "git pull origin ${branchName}"
    } else {
      steps.sh "git switch -c ${branchName}"
    }
  }
}
