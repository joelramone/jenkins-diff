import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.orchestrator.PipelineOrchestrator

void call(Map args = [:]) {
  PipelineConfiguration configuration = PipelineConfiguration.fromRuntime(this, args)
  PipelineOrchestrator orchestrator = new PipelineOrchestrator(this, configuration)
  orchestrator.execute()
}
