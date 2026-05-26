@Library('vault-shared-library') _

pipeline {

    agent any

    stages {

        stage('Load Config') {

            steps {

                script {

                    def props = readProperties file: 'config.properties'

                    vaultPipeline(

                        SLACK_CHANNEL_NAME  : props.SLACK_CHANNEL_NAME,
                        ENVIRONMENT         : props.ENVIRONMENT,
                        CODE_BASE_PATH      : props.CODE_BASE_PATH,
                        ACTION_MESSAGE      : props.ACTION_MESSAGE,
                        KEEP_APPROVAL_STAGE : props.KEEP_APPROVAL_STAGE,
                        PLAYBOOK            : props.PLAYBOOK,
                        INVENTORY           : props.INVENTORY,
                        GIT_REPO            : props.GIT_REPO
                    )
                }
            }
        }
    }
}
