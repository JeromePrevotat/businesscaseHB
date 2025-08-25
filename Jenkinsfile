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

        // DB
        DB_HOST = 'mysql'
        DB_PORT = '3306'
        DB_NAME = 'db'
        DB_USER = 'user'
        DB_PASSWORD = 'password'
        
        // Test DB
        DB_TEST_HOST = 'mysql-test'
        DB_TEST_PORT = '3307'
        DB_TEST_NAME = 'db-test'
        DB_TEST_USER = 'user'
        DB_TEST_PASSWORD = 'password'
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