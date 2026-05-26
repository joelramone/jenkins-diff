import com.company.cicd.config.PipelineConfiguration
import com.company.cicd.orchestrator.PipelineOrchestrator

void call(Map args = [:]) {
  PipelineConfiguration configuration = PipelineConfiguration.fromArgs(args)
  PipelineOrchestrator orchestrator = new PipelineOrchestrator(this, configuration)
  orchestrator.execute()
}
