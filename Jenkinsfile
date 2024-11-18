pipeline {
    agent any
    tools {
	    maven "M3"
	    jdk "java17"
	}

    environment {
        registryCredential = 'e101b1e7-9eed-4665-bedf-672243aad7fb'
        appRegistry = "ghcr.io/nnbros/rtp-gateway"
        rtpRegistry = "https://ghcr.io"
    }
    stages {
        stage('Fetch code'){
            steps {
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[credentialsId: registryCredential, url: 'https://github.com/nnbros/rtp-story-teller.git']])
            }
        }
        stage('Build artifact') {
            steps {
                sh 'mvn -B install'
            }
        }
        stage('Build Docker Image') {
            steps {
                withCredentials([string(credentialsId: 'RTP_DEV_1_TOKEN', variable: 'BOT_TOKEN')]){
                    script {
                        dockerImage = docker.build( appRegistry + ":$BUILD_NUMBER", "--build-arg BOT_TOKEN=$BOT_TOKEN .")
                    }
                }
            }
        }
        stage('Upload Docker Image') {
            environment {
                VERSION = sh ( script: 'echo $(xmlstarlet sel -N p="http://maven.apache.org/POM/4.0.0" -t -v "/p:project/p:version" -n pom.xml)', returnStdout: true).trim()
            }
            steps{
                script {
                    docker.withRegistry( rtpRegistry, registryCredential ) {
                        dockerImage.push("$VERSION")
                        dockerImage.push("latest")
                    }
                }
            }
        }
        stage('Deploy to rtp-dev') {
            steps {
                script{
                    def remote = [:]
                    remote.name = "rtp-dev"
                    remote.host = "192.168.200.78"
                    remote.port = 2207
                    remote.user = "jenkins"
                    remote.knownHosts = "/var/lib/jenkins/.ssh/known_hosts"
                    remote.identityFile = "/var/lib/jenkins/.ssh/jenkins_rsa"
                    sshCommand remote: remote, command: "docker compose -f /opt/rtp/resources/compose.yml down"
                    sshCommand remote: remote, command: "docker compose -f /opt/rtp/resources/compose.yml up -d"
                }
            }
        }
    }
}
