package com.company.cicd.context

import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.types.EnvironmentConfig

class DeploymentContext implements Serializable {
  def steps
  PipelineConfiguration configuration
  EnvironmentConfig environment

  DeploymentContext(def steps, PipelineConfiguration configuration, EnvironmentConfig environment) {
    this.steps = steps
    this.configuration = configuration
    this.environment = environment
  }
}
