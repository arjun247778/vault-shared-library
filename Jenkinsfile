@Library('vault-lib') _

def config = [

    SLACK_CHANNEL_NAME  : 'build-status',
    ENVIRONMENT         : 'prod',
    CODE_BASE_PATH      : 'env/prod',
    ACTION_MESSAGE      : 'Vault Deployment Started',
    KEEP_APPROVAL_STAGE : true,

    REPO_URL            : 'https://github.com/your-repo.git'
]

vaultDeploy(config)
