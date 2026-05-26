def call(props) {

    stage('Clone') {

        echo "Cloning Repository"

        git "${props.GIT_REPO}"
    }

    stage('User Approval') {

        if (props.KEEP_APPROVAL_STAGE == "true") {

            input message: "Approve Vault Deployment?"
        }
    }

    stage('Playbook Execution') {

        sh """
            ansible-playbook -i ${props.INVENTORY} ${props.PLAYBOOK}
        """
    }

    stage('Notification') {

        slackSend(
            channel: "${props.SLACK_CHANNEL_NAME}",
            message: "SUCCESS : ${props.ACTION_MESSAGE}"
        )
    }
}