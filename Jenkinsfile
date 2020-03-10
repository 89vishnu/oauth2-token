#!/usr/bin/env groovy
import groovy.json.JsonSlurper

pipeline {
    agent any

    //General Options
    options {
        buildDiscarder(logRotator(daysToKeepStr: '30', numToKeepStr: '5'))
    }

    parameters {
        string(name: 'REGION_NAME',     defaultValue: 'us-east-2', description: 'Aws Region')
        string(name: 'ECR_URL',         defaultValue: '461403996979.dkr.ecr.us-east-2.amazonaws.com', description: 'AWS Repo')
        string(name: 'TAG_VERSION',     defaultValue: '1.0', description: 'Build Version')
        string(name: 'VPC_ID',          defaultValue: 'vpc-835c75ea', description: 'AWS VPC ID')
        string(name: 'SUBNET_IDS',      defaultValue: 'subnet-8cf58cf7', description: 'AWS Subnet Id - comma seperated')
        string(name: 'SECURITY_GROUP',  defaultValue: 'sg-ef651784', description: 'Security Group for the ECS')
        string(name: 'IAM_ROLE',        defaultValue: 'ecsInstanceRole', description: 'IAM Role')
        string(name: 'ECR_DOCKER_COMPOSE_FILE', defaultValue: 'ecr-docker-compose.yaml', description: 'Compose file for Ecr cli')
        string(name: 'CLUSTER_NAME',    defaultValue: 'creator-dancesterz', description: 'ECS Cluster Name')
        string(name: 'PROJECT_NAME',    defaultValue: 'creator-danster', description: 'ECS Cluster Name')
        choice(name: 'TARGET_GROUP_ARN',choices: 'arn:aws:elasticloadbalancing:us-east-2:461403996979:targetgroup/danster-dev-target/dc77f50a5c498c5f\n' +
                'arn:aws:elasticloadbalancing:us-east-2:461403996979:targetgroup/prod-dancesterz-v2-tg/8a40cd758f3a7937\n' +
                'arn:aws:elasticloadbalancing:us-east-2:461403996979:targetgroup/prod-dancesterz-tg/c6d3c8afeaf104a5', description: 'AWS LOAD balancer target group')
        choice(name: 'DISERED_NO_OF_TASK', choices: '1\n2\n3\n4\n5', description: 'Number of Containers Per Cluster')
        booleanParam(name: 'BUILD_SOURCE', defaultValue: true, description: 'Deploy One Instance [true] or Only Scale [false] ?')
        booleanParam(name: 'DEPLOY_AND_SCALE', defaultValue: true, description: 'Deploy One Instance [true] or Only Scale [false] ?')
        booleanParam(name: 'DEPLOY_DEV', defaultValue: true, description: 'ECS Cluster Name')
        booleanParam(name: 'DEPLOY_PROD', defaultValue: false, description: 'ECS Cluster Name')

        booleanParam(name: 'CREATE_CLUSTER', defaultValue: false, description: 'Create Cluster')
        string(name: 'EC2_INSTANCE_TYPE',    defaultValue: 't2.small', description: 'Instance Size [t2.medium, t2.large]')
        string(name: 'CLUSTER_SIZE',    defaultValue: '1', description: 'Instance count in cluster')
        booleanParam(name: 'DROP_CLUSTER', defaultValue: false, description: 'Drop ECS Cluster')

        booleanParam(name: 'DEPLOY_NOTIFICATION', defaultValue: false, description: 'Dansters Notification Deployment')
    }

    environment {
        //BUILD_AND_DEPLOY='true'
        DOCKER_REGISTRY ='192.168.2.146:5000'
    }


    stages {

        stage('Create or Remove ECS Cluster') {
            when {
                expression {
                    return params.CREATE_CLUSTER || params.DROP_CLUSTER
                }
            }
            steps {
                script {
                    if(params.DEPLOY_DEV){
                        env.ENVIRONMENT = 'dev'
                    }
                    if(params.DEPLOY_PROD){
                        env.ENVIRONMENT = 'prod'
                    }

                    if(params.CREATE_CLUSTER){
                        createCluster()
                    }
                    if(params.DROP_CLUSTER){
                        deleteCluster()
                    }
                    currentBuild.result = 'SUCCESS';
                    return
                }
            }
        }

        stage('Build/Package') {
            when { expression { return params.DEPLOY_AND_SCALE && params.BUILD_SOURCE } }

            steps {
                echo 'Building..'
                echo "Running ${env.BUILD_ID} on ${env.JENKINS_URL} with ${DOCKER_REGISTRY} ${env.DOCKER_REGISTRY} $DOCKER_REGISTRY"
                sh 'mvn clean package -DskipTests'
                echo 'Application Build Completed ..'
            }
        }

        stage('Create Docker Image') {
            when { expression { return params.DEPLOY_AND_SCALE && params.BUILD_SOURCE } }

            steps {
                echo 'Create Docker Image..'
                sh 'echo $USER $DOCKER_REGISTRY'
                sh 'export DOCKER_REGISTRY=$DOCKER_REGISTRY; ' +
                        'export VERSION=$BUILD_NUMBER;' +
                        'echo $VERSION $DOCKER_REGISTRY $AWS_APP_ACCESS_KEY_ID; ' +
                        'docker-compose -f docker-compose-monolithic.yml build'
            }
        }

        stage('Push Docker Image') {
            when {
                expression {
                    return params.DEPLOY_AND_SCALE && params.BUILD_SOURCE && (params.DEPLOY_DEV || params.DEPLOY_PROD)
                }
            }

            steps {
                sh 'eval $(aws ecr get-login --no-include-email --region ${REGION_NAME} | sed \'s|https://||\' ) '
                sh 'docker tag $DOCKER_REGISTRY/dancesters-web:$BUILD_NUMBER ${ECR_URL}/dancesters-web:${TAG_VERSION} '
                sh 'docker push ${ECR_URL}/dancesters-web:${TAG_VERSION}'
                sh 'docker rmi $DOCKER_REGISTRY/dancesters-web:$BUILD_NUMBER'
                sh 'docker tag $DOCKER_REGISTRY/danster-notification:$BUILD_NUMBER ${ECR_URL}/danster-notification:${TAG_VERSION} '
                sh 'docker push ${ECR_URL}/danster-notification:${TAG_VERSION}'
                sh 'docker rmi $DOCKER_REGISTRY/danster-notification:$BUILD_NUMBER'
            }
        }

        stage('Deploy To Development') {
            when {
                expression {
                    return params.DEPLOY_AND_SCALE  && params.DEPLOY_DEV
                }
            }
            steps {
                script {
                    env.ENVIRONMENT = 'dev'
                    if(params.DEPLOY_NOTIFICATION) {
                        deployNotificationDockerContainer()
                    } else {
                        deployDockerContainer()
                    }
                }
            }
        }

        stage('Deploy To Production') {
            when {
                expression {
                    return params.DEPLOY_AND_SCALE  && params.DEPLOY_PROD
                }
            }
            steps {
                script {
                    env.ENVIRONMENT = 'prod'
                    if(params.DEPLOY_NOTIFICATION) {
                        deployNotificationDockerContainer()
                    } else {
                        deployDockerContainer()
                    }
                }
            }
        }

        stage('Scale Environment') {
            when {
                expression {
                    return !params.DEPLOY_AND_SCALE || (params.DEPLOY_DEV && params.DISERED_NO_OF_TASK > 1)
                }
            }
            steps {
                script {
                    env.ENVIRONMENT = 'dev'
                    deployDockerContainerScale()
                }
            }
        }
    }

}

def deployDockerContainerScale(){
    dir('./') {
        echo 'Scaling to ' + env.ENVIRONMENT
        script {
            env.TMP_FILE = env.ENVIRONMENT + "_" + params.ECR_DOCKER_COMPOSE_FILE
        }

        sh 'cat ${ECR_DOCKER_COMPOSE_FILE} | sed "s/\\$ENV/$ENVIRONMENT/g;s/\\$TAG/$TAG_VERSION/g;s/\\$REGION/$REGION_NAME/g;s/\\$DOCKER_REGISTRY/$ECR_URL/g;" > ${TMP_FILE}'
        sh 'cat ${TMP_FILE}'

        sh 'ecs-cli compose --project-name ${PROJECT_NAME} --file ${TMP_FILE} --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} service scale ${DISERED_NO_OF_TASK} --deployment-max-percent 100 --deployment-min-healthy-percent 0 --timeout 10'
    }
}

@NonCPS
def createCluster(){
    sh 'ecs-cli up --launch-type EC2 --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} --size ${CLUSTER_SIZE} --instance-type ${EC2_INSTANCE_TYPE} --vpc ${VPC_ID} --subnets ${SUBNET_IDS} --security-group ${SECURITY_GROUP} --instance-role ${IAM_ROLE} --no-associate-public-ip-address --force'
}

def deleteCluster(){
    sh 'ecs-cli down --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} --force'
}

def deployDockerContainer(){
    dir('./'){
        echo 'Deploying to ' + env.ENVIRONMENT

        script {
            env.TMP_FILE = env.ENVIRONMENT + "_" + params.ECR_DOCKER_COMPOSE_FILE
        }

        sh 'cat ${ECR_DOCKER_COMPOSE_FILE} | sed "s/\\$ENV/$ENVIRONMENT/g;s/\\$TAG/$TAG_VERSION/g;s/\\$REGION/$REGION_NAME/g;s/\\$DOCKER_REGISTRY/$ECR_URL/g;" > ${TMP_FILE}'
        sh 'cat ${TMP_FILE}'

        timeout(time: 2, unit: 'MINUTES'){
            script {
                waitUntil {
                    script {
                        def desClusterRS    = sh(returnStdout: true, script: "aws ecs describe-clusters --cluster ${ENVIRONMENT}-${CLUSTER_NAME} --region ${REGION_NAME}").trim()
                        def desClusterJSON  = new JsonSlurper().parseText(desClusterRS)
                        def regContainerCnt = desClusterJSON.clusters[0].registeredContainerInstancesCount
                        return (regContainerCnt > 0)
                    }
                }
            }
        }

        sh 'ecs-cli compose --project-name ${PROJECT_NAME} --ecs-params ecs-params.yml --file ${TMP_FILE} --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} service up --force-deployment --target-group-arn ${TARGET_GROUP_ARN} --container-name dancesters-web --health-check-grace-period 180 --container-port 8080 --create-log-groups --deployment-max-percent 100 --deployment-min-healthy-percent 0  --timeout 10'



        if(params.DEPLOY_NOTIFICATION){
            sh 'ecs-cli compose --project-name creator-danster-notification --ecs-params ecs-params.yml --file ${TMP_FILE} --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} service up --force-deployment --container-name dancesters-notification --container-port 8081 --create-log-groups --deployment-max-percent 100 --deployment-min-healthy-percent 0 --timeout 10'
        }
    }
}


def deployNotificationDockerContainer(){
    dir('./'){
        echo 'Deploying to ' + env.ENVIRONMENT

        script {
            env.TMP_FILE = env.ENVIRONMENT + "_notification-" + params.ECR_DOCKER_COMPOSE_FILE
        }

        sh 'cat notification-${ECR_DOCKER_COMPOSE_FILE} | sed "s/\\$ENV/$ENVIRONMENT/g;s/\\$TAG/$TAG_VERSION/g;s/\\$REGION/$REGION_NAME/g;s/\\$DOCKER_REGISTRY/$ECR_URL/g;" > ${TMP_FILE}'
        sh 'cat ${TMP_FILE}'

        if(params.DEPLOY_NOTIFICATION){

            sh 'ecs-cli compose --project-name creator-danster-notification --ecs-params ecs-params.yml --file ${TMP_FILE} --region ${REGION_NAME} --cluster ${ENVIRONMENT}-${CLUSTER_NAME} service up --force-deployment --create-log-groups --deployment-max-percent 100 --deployment-min-healthy-percent 0 --timeout 10'
        }
    }
}

