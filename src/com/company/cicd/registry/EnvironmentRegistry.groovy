package com.company.cicd.registry

import com.company.cicd.enums.DeploymentType
import com.company.cicd.enums.EnvironmentType
import com.company.cicd.types.EnvironmentConfig

class EnvironmentRegistry implements Serializable {
  private final Map<EnvironmentType, EnvironmentConfig> environments
  private final Map<String, List<EnvironmentType>> deploymentGroups
  private final Map<String, List<EnvironmentType>> parallelGroups

  EnvironmentRegistry() {
    this.environments = [
      (EnvironmentType.DEV): create(EnvironmentType.DEV, DeploymentType.SINGLE_SITE, 'lower', false, 15, 1, false, false),
      (EnvironmentType.TESTING): create(EnvironmentType.TESTING, DeploymentType.SINGLE_SITE, 'lower', true, 20, 2, false, false),
      (EnvironmentType.LABORATORIO): create(EnvironmentType.LABORATORIO, DeploymentType.SINGLE_SITE, 'lower', true, 20, 1, false, false),
      (EnvironmentType.QA): create(EnvironmentType.QA, DeploymentType.MULTI_SITE, 'upper', true, 30, 2, false, false),
      (EnvironmentType.HOMO): create(EnvironmentType.HOMO, DeploymentType.MULTI_SITE, 'upper', true, 30, 1, false, false),
      (EnvironmentType.PROD): create(EnvironmentType.PROD, DeploymentType.MULTI_SITE, 'production', true, 60, 1, true, true)
    ]

    this.deploymentGroups = [
      multisite: [EnvironmentType.PROD, EnvironmentType.HOMO, EnvironmentType.QA],
      singlesite: [EnvironmentType.TESTING, EnvironmentType.LABORATORIO, EnvironmentType.DEV]
    ]

    this.parallelGroups = [
      lower: [EnvironmentType.DEV, EnvironmentType.TESTING, EnvironmentType.LABORATORIO],
      upper: [EnvironmentType.QA, EnvironmentType.HOMO]
    ]
  }

  private static EnvironmentConfig create(EnvironmentType env, DeploymentType type, String group, boolean approval, int timeout, int retries, boolean sequential, boolean failFast) {
    new EnvironmentConfig(environment: env, deploymentType: type, parallelGroup: group, approvalRequired: approval, timeoutMinutes: timeout, retries: retries, sequential: sequential, failFast: failFast)
  }

  EnvironmentConfig get(EnvironmentType env) { environments[env] }
  List<EnvironmentConfig> lowerGroup() { parallelGroups.lower.collect { environments[it] } }
  List<EnvironmentConfig> upperGroup() { parallelGroups.upper.collect { environments[it] } }
  EnvironmentConfig production() { environments[EnvironmentType.PROD] }
}
