package com.company.cicd.strategies

import com.company.cicd.context.DeploymentContext

class MultiSiteDeploymentStrategy implements DeploymentStrategy {
  @Override
  void deploy(DeploymentContext context) {
    context.steps.sh "chmod +x k8s/helm/setup/deploy_multisite_command.sh"
    context.steps.sh "k8s/helm/setup/deploy_multisite_command.sh ${context.environment.stageName()}"
  }
}
