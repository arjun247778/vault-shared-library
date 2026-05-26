def call(Map config = [:]) {

    pipeline {

        agent any

        environment {

            SLACK_CHANNEL_NAME  = config.SLACK_CHANNEL_NAME
            ENVIRONMENT         = config.ENVIRONMENT
            CODE_BASE_PATH      = config.CODE_BASE_PATH
            ACTION_MESSAGE      = config.ACTION_MESSAGE
            KEEP_APPROVAL_STAGE = config.KEEP_APPROVAL_STAGE
            PLAYBOOK            = config.PLAYBOOK
            INVENTORY           = config.INVENTORY
            GIT_REPO            = config.GIT_REPO
        }

        stages {

            stage('Clone') {

                steps {

                    echo "Cloning Repository"

                    git "${GIT_REPO}"
                }
            }

            stage('User Approval') {

                when {
                    expression {
                        return KEEP_APPROVAL_STAGE == "true"
                    }
                }

                steps {

                    input message: "Approve Vault Deployment?"
                }
            }

            stage('Playbook Execution') {

                steps {

                    sh """
                        ansible-playbook -i ${INVENTORY} ${PLAYBOOK}
                    """
                }
            }
        }

        post {

            success {

                slackSend(
                    channel: "${SLACK_CHANNEL_NAME}",
                    message: "SUCCESS : ${ACTION_MESSAGE}"
                )
            }

            failure {

                slackSend(
                    channel: "${SLACK_CHANNEL_NAME}",
                    message: "FAILED : ${ACTION_MESSAGE}"
                )
            }
        }
    }
}