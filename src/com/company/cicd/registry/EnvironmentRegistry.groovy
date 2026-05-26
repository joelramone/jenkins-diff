package com.company.cicd.registry

import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.enums.DeploymentType
import com.company.cicd.types.EnvironmentConfig

class EnvironmentRegistry implements Serializable {
  private final Map<String, EnvironmentConfig> environments = [:]
  private final Map<String, List<String>> parallelGroups = [:]

  EnvironmentRegistry(def steps, PipelineConfiguration configuration) {
    Map environmentsYaml = steps.readYaml(file: 'pipeline-config/environments.yaml') ?: [:]
    Map groupsYaml = steps.readYaml(file: 'pipeline-config/deployment-groups.yaml') ?: [:]

    Map<String, Map> envData = (Map<String, Map>) (environmentsYaml.environments ?: [:])
    envData.each { String name, Map data ->
      environments[name] = new EnvironmentConfig(
        name: name,
        deploymentType: resolveDeploymentType(data.topology?.toString()),
        parallelGroup: (data.parallelGroup ?: '') as String,
        approvalRequired: (data.approval ?: false) as boolean,
        timeoutMinutes: (data.timeout ?: configuration.defaultTimeout) as int,
        retries: (data.retries ?: configuration.defaultRetries) as int,
        sequential: (data.sequential ?: false) as boolean,
        failFast: (data.failFast ?: configuration.globalFailFast) as boolean
      )
    }

    Map<String, List> groups = (Map<String, List>) (groupsYaml.parallelGroups ?: [:])
    groups.each { String groupName, List groupEnvs ->
      parallelGroups[groupName] = groupEnvs.collect { it.toString() }
    }
  }

  private static DeploymentType resolveDeploymentType(String topology) {
    topology == 'multi-site' ? DeploymentType.MULTI_SITE : DeploymentType.SINGLE_SITE
  }

  List<EnvironmentConfig> byGroup(String groupName) {
    (parallelGroups[groupName] ?: []).collect { environments[it] }.findAll { it != null }
  }

  EnvironmentConfig production() {
    byGroup('production').first()
  }
}
