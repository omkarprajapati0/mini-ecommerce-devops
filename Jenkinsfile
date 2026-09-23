pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Build') {
            steps {
                bat 'mvn -B -DskipTests package'
            }
        }

        stage('Selenium Test') {
            steps {
                bat 'mvn -B test'
            }
            post {
                always {
                    junit allowEmptyResults: true,
                          testResults: 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Docker Build') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    bat 'docker build -t %DOCKER_USER%/mini-ecommerce-devops:%BUILD_NUMBER% -t %DOCKER_USER%/mini-ecommerce-devops:latest .'
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-creds',
                        usernameVariable: 'DOCKER_USER',
                        passwordVariable: 'DOCKER_TOKEN'
                    )
                ]) {
                    bat '''
@echo off
powershell -NoProfile -Command "$env:DOCKER_TOKEN | docker login --username $env:DOCKER_USER --password-stdin"
if errorlevel 1 exit /b 1
docker push %DOCKER_USER%/mini-ecommerce-devops:%BUILD_NUMBER%
if errorlevel 1 exit /b 1
docker push %DOCKER_USER%/mini-ecommerce-devops:latest
if errorlevel 1 exit /b 1
docker logout
'''
                }
            }
        }
    }
}