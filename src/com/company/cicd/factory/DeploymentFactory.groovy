package com.company.cicd.factory

import com.company.cicd.enums.DeploymentType
import com.company.cicd.strategies.DeploymentStrategy
import com.company.cicd.strategies.MultiSiteDeploymentStrategy
import com.company.cicd.strategies.SingleSiteDeploymentStrategy

class DeploymentFactory implements Serializable {
  DeploymentStrategy create(DeploymentType type) {
    switch (type) {
      case DeploymentType.SINGLE_SITE:
        return new SingleSiteDeploymentStrategy()
      case DeploymentType.MULTI_SITE:
        return new MultiSiteDeploymentStrategy()
      default:
        throw new IllegalArgumentException("Unsupported deployment type: ${type}")
    }
  }
}
