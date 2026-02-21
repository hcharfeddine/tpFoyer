pipeline {
    agent any

    environment {
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
                withCredentials([usernamePassword(
                    credentialsId: 'nexus-credentials', 
                    usernameVariable: 'NEXUS_USERNAME', 
                    passwordVariable: 'NEXUS_PASSWORD'
                )]) {
                    script {
                        def mavenHome = tool 'Maven 3.8.6'
                        
                        // Créer le fichier settings.xml avec les credentials
                        sh '''
                            cat > settings.xml << 'EOF'
<settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              http://maven.apache.org/xsd/settings-1.0.0.xsd">
    <servers>
        <server>
            <id>nexus-snapshots</id>
            <username>${NEXUS_USERNAME}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
        <server>
            <id>nexus-releases</id>
            <username>${NEXUS_USERNAME}</username>
            <password>${NEXUS_PASSWORD}</password>
        </server>
    </servers>
</settings>
EOF
                        '''
                        
                        // Déployer vers Nexus
                        sh "${mavenHome}/bin/mvn deploy -DskipTests -s settings.xml"
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Utiliser les commandes shell Docker directement
                    sh """
                        docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} .
                    """
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    // Pousser l'image vers le registre local
                    sh """
                        docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                    """
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