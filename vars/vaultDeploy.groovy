def call(Map config) {

    pipeline {

        agent any

        stages {

            stage('Clone Repository') {

                steps {

                    git branch: 'main',
                    url: config.REPO_URL
                }
            }

            stage('User Approval') {

                when {
                    expression {
                        config.KEEP_APPROVAL_STAGE == true
                    }
                }

                steps {

                    input(
                        message: "Deploy to ${config.ENVIRONMENT} ?",
                        ok: 'Approve'
                    )
                }
            }

            stage('Vault Authentication') {

                steps {

                    withCredentials([
                        string(
                            credentialsId: 'vault-token',
                            variable: 'VAULT_TOKEN'
                        )
                    ]) {

                        sh '''
                        export VAULT_ADDR=http://localhost:8200

                        vault login $VAULT_TOKEN

                        vault kv get secret/myapp
                        '''
                    }
                }
            }

            stage('Deployment') {

                steps {

                    echo "Deployment Started"

                    sh """
                    echo "Environment: ${config.ENVIRONMENT}"
                    echo "Code Path: ${config.CODE_BASE_PATH}"
                    """
                }
            }
        }

        post {

            success {

                slackSend(
                    channel: "#${config.SLACK_CHANNEL_NAME}",
                    message: """
SUCCESS

Environment: ${config.ENVIRONMENT}

${config.ACTION_MESSAGE}
"""
                )
            }

            failure {

                slackSend(
                    channel: "#${config.SLACK_CHANNEL_NAME}",
                    message: """
FAILED

Environment: ${config.ENVIRONMENT}

Check Jenkins Console
"""
                )
            }
        }
    }
}
