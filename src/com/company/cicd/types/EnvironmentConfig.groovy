package com.company.cicd.types

import com.company.cicd.enums.DeploymentType
import com.company.cicd.enums.EnvironmentType

class EnvironmentConfig implements Serializable {
  EnvironmentType environment
  DeploymentType deploymentType
  String parallelGroup
  boolean approvalRequired
  int timeoutMinutes
  int retries
  boolean sequential
  boolean failFast

  String stageName() {
    environment.name().toLowerCase()
  }
}
