package com.company.cicd.types

import com.company.cicd.enums.DeploymentType

class EnvironmentConfig implements Serializable {
  String name
  DeploymentType deploymentType
  String parallelGroup
  boolean approvalRequired
  int timeoutMinutes
  int retries
  boolean sequential
  boolean failFast

  String stageName() {
    name
  }
}
