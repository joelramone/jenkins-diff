package com.company.cicd.managers

class GitManager implements Serializable {
  private final def steps

  GitManager(def steps) { this.steps = steps }

  void configureIdentity(String name, String email) {
    steps.sh "git config user.name '${name}'"
    steps.sh "git config user.email '${email}'"
  }

  boolean hasChanges() {
    String status = steps.sh(script: 'git status --porcelain', returnStdout: true).trim()
    !status.isEmpty()
  }

  String diffSummary() {
    steps.sh(script: 'git diff --stat', returnStdout: true).trim()
  }

  void commitTrackedChanges(String message) {
    steps.sh 'git add -A'
    steps.sh "git commit -m '${message}'"
  }

  void push(String branch) {
    steps.sh "git push origin ${branch}"
  }
}
