@Library('vault-shared-library') _

pipeline {

    agent any

    stages {

        stage('Load Config') {

            steps {

                script {

                    def props = readProperties file: 'config.properties'

                    vaultPipeline(props)
                }
            }
        }
    }
}