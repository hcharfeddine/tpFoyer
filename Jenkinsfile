pipeline {
    agent any

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "127.0.0.1:8081"
        NEXUS_REPOSITORY = "maven-releases"
        NEXUS_CREDENTIAL_ID = "nexus-credentials"
        DOCKER_IMAGE = "my-app:latest"
        KUBECONFIG_ID = "kubeconfig"
    }

    tools {
        maven 'Maven 3.8.6'
        jdk 'JDK 17'
    }

    stages {

        stage('Checkout') {
            steps {
                script {
                    // Skip git checkout if the directory is already a git repo (like in Replit)
                    if (!fileExists('.git')) {
                        git branch: 'main', url: 'https://github.com/hcharfeddine/tpFoyer.git'
                    }
                }
            }
        }

        stage('Compile') {
            steps {
                script {
                    def mavenHome = tool 'Maven 3.8.6'
                    sh "${mavenHome}/bin/mvn clean compile -DskipTests"
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def mavenHome = tool 'Maven 3.8.6'
                    withSonarQubeEnv('SonarQube') {
                        sh "${mavenHome}/bin/mvn sonar:sonar -DskipTests"
                    }
                }
            }
        }

        stage('Package') {
            steps {
                script {
                    def mavenHome = tool 'Maven 3.8.6'
                    sh "${mavenHome}/bin/mvn package -DskipTests"
                }
            }
        }

        stage('Deploy to Nexus') {
    steps {
        withCredentials([usernamePassword(credentialsId: 'nexus-credentials', 
                                          usernameVariable: 'admin', 
                                          passwordVariable: 'admin')]) {
            script {
                def mavenHome = tool 'Maven 3.8.6'
                
                sh """
                    cat > settings.xml << 'EOF'
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>nexus-snapshots</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
        <server>
            <id>nexus-releases</id>
            <username>${NEXUS_USER}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
EOF
                    ${mavenHome}/bin/mvn deploy -DskipTests -s settings.xml
                """
            }
        }
    }
}




        stage('Docker Build') {
            steps {
                script {
                    dockerImage = docker.build("${DOCKER_IMAGE}")
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        dockerImage.push()
                    }
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withKubeConfig([credentialsId: KUBECONFIG_ID]) {
                    sh 'kubectl apply -f k8s/deployment.yaml'
                    sh 'kubectl apply -f k8s/service.yaml'
                }
            }
        }

    } 
} 
