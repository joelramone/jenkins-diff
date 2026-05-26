package com.company.cicd.adapters

class LegacyApprovalAdapter implements Serializable {
  private final def steps

  LegacyApprovalAdapter(def steps) {
    this.steps = steps
  }

  void request(String environmentName) {
    steps.aprobaciones.request(environmentName)
  }
}
