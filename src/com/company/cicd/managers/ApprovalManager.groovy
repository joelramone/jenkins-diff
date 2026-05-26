package com.company.cicd.managers

import com.company.cicd.adapters.LegacyApprovalAdapter
import com.company.cicd.types.EnvironmentConfig

class ApprovalManager implements Serializable {
  private final LegacyApprovalAdapter adapter

  ApprovalManager(LegacyApprovalAdapter adapter) {
    this.adapter = adapter
  }

  void approveIfRequired(EnvironmentConfig environment) {
    if (environment.approvalRequired) {
      adapter.request(environment.stageName())
    }
  }
}
