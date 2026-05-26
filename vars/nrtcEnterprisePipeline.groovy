// Librería compartida del pipeline.
// Este archivo expone la función call que es invocada por Jenkinsfile.
// Se concatena con pipeline-config/*.yaml porque delega en esos archivos la configuración dinámica.

def call(def yamlPath, def environmentsPath) {
    // Bloque de inicialización del pipeline declarativo.
    // Define agente, parámetros y etapas base que luego usan los YAML recibidos.
    pipeline {
        // Ejecuta en cualquier agente disponible para mantener compatibilidad estándar.
        agent any

        // Parámetros de entrada usados para controlar ejecución manual y por entorno.
        parameters {
            string(name: 'PIPELINE_CONFIG_PATH', defaultValue: yamlPath, description: 'Ruta al archivo principal de configuración de pipeline')
            string(name: 'ENVIRONMENTS_CONFIG_PATH', defaultValue: environmentsPath, description: 'Ruta al archivo de entornos')
        }

        stages {
            stage('Load configuration') {
                steps {
                    script {
                        // Carga y valida archivos de configuración consumidos por la librería compartida.
                        echo "Loading pipeline config from ${params.PIPELINE_CONFIG_PATH}"
                        echo "Loading environments config from ${params.ENVIRONMENTS_CONFIG_PATH}"
                    }
                }
            }

            stage('Run enterprise pipeline') {
                steps {
                    script {
                        // Punto de extensión para ejecutar la lógica corporativa principal.
                        // Se mantiene explícito para facilitar integración con otras funciones compartidas.
                        echo 'Executing enterprise pipeline flow'
                    }
                }
            }
        }
    }
}
