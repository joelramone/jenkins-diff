package com.company.cicd.managers

import com.company.cicd.context.DeploymentContext
import com.company.cicd.factory.DeploymentFactory

class DeploymentManager implements Serializable {
  private final DeploymentFactory factory

  DeploymentManager(DeploymentFactory factory) {
    this.factory = factory
  }

  void deploy(DeploymentContext context) {
    def strategy = factory.create(context.environment.deploymentType)
    strategy.deploy(context)
  }
}
