package com.company.cicd.strategies

import com.company.cicd.context.DeploymentContext

interface DeploymentStrategy extends Serializable {
  void deploy(DeploymentContext context)
}
