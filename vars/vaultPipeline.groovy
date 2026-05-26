def call(Map config = [:]) {

    pipeline {

        agent any

        stages {

            stage('Clone') {

                steps {

                    echo "Cloning Repository"

                    git "${config.GIT_REPO}"
                }
            }

            stage('User Approval') {

                when {
                    expression {
                        return config.KEEP_APPROVAL_STAGE == "true"
                    }
                }

                steps {

                    input message: "Approve Vault Deployment?"
                }
            }

            stage('Playbook Execution') {

                steps {

                    sh """
                        ansible-playbook -i ${config.INVENTORY} ${config.PLAYBOOK}
                    """
                }
            }
        }

        post {

            success {

                slackSend(
                    channel: "${config.SLACK_CHANNEL_NAME}",
                    message: "SUCCESS : ${config.ACTION_MESSAGE}"
                )
            }

            failure {

                slackSend(
                    channel: "${config.SLACK_CHANNEL_NAME}",
                    message: "FAILED : ${config.ACTION_MESSAGE}"
                )
            }
        }
    }
}