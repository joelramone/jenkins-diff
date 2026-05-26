// Archivo orquestador principal de Jenkins.
// Se conecta con vars/nrtcEnterprisePipeline.groovy para ejecutar la lógica compartida.
// También consume pipeline-config/pipeline.yaml y pipeline-config/environments.yaml
// para definir etapas y comportamiento por entorno.

@Library("nrtc-shared-library") _

nrtcEnterprisePipeline("pipeline-config/pipeline.yaml", "pipeline-config/environments.yaml")
