package com.company.cicd.adapters

class LegacyDeploymentAdapter implements Serializable {
  private final def steps

  LegacyDeploymentAdapter(def steps) {
    this.steps = steps
  }

  void runLegacyIntegration() {
    steps.IntegracionNRTC2()
  }

  void runLegacyApplyDeployment() {
    steps.AplicamosDespliegueNRTC()
  }
}
