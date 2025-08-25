pipeline{
    agent any
    tools {
        maven 'mvn'
    }
    environment{
        // JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
        JAVA_HOME = '/opt/java/openjdk'
        PATH = "${JAVA_HOME}/bin:${env.PATH}"
        IMAGE_NAME = 'spring-jenkins'
        IMAGE_TAG = 'latest'
    }
    stages {
        stage ('Checkout Branch Main'){
            steps {
                git branch: 'main', url: 'https://github.com/JeromePrevotat/BuisinessCase.git'
            }
        }
        stage ('Build'){
            steps{
                sh 'mvn compile'
            }
        }
        stage ('Tests'){
            steps{
                sh 'docker compose up -d mysql mysql-test'
                sh 'mvn test -Dtest=PlugTypeRepositoryTests'
                // sh 'mvn test'
            }
        }
        stage('SonarQube') {
            steps {
                sh 'mvn sonar:sonar -Dsonar.login=${env.SONAR_TOKEN}'
            }
        }
        stage ('Packaging'){
            steps{
                sh 'mvn package'
            }
        }
        stage ('Docker Build'){
            steps{
                sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} .'
            }
        }
       stage ('Deployment'){
            steps{
                sh """
                docker stop ${IMAGE_NAME} || true
                docker rm ${IMAGE_NAME} || true
                docker run -d --name ${IMAGE_NAME} -p 8081:8080 ${IMAGE_NAME}:${IMAGE_TAG}
                """
            }
        }
        stage ('Archive Reports'){
            steps{
                junit 'target/surefire/*.xml'
            }
        }
        stage ('Archive Artifact'){
            steps{
                archiveArtifacts artifacts: 'target/*.jar'
                
            }
        }
    }
}