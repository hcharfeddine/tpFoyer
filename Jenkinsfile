pipeline { 
    agent any

    environment {
        NEXUS_VERSION = "nexus3"
        NEXUS_PROTOCOL = "http"
        NEXUS_URL = "127.0.0.1:8081"
        NEXUS_SNAPSHOT_REPO = "nexus-snapshots"
        NEXUS_RELEASE_REPO = "maven-releases"
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
        script {
            def pom = readMavenPom()
            def version = pom.getVersion()
            def artifactFile = "target/tpFoyer-17-${version}.jar"
            
            if (!fileExists(artifactFile)) {
                error "Artifact ${artifactFile} not found!"
            }

            // Choose repository
            def repository = version.endsWith('-SNAPSHOT') ? NEXUS_SNAPSHOT_REPO : NEXUS_RELEASE_REPO

            nexusArtifactUploader(
                nexusVersion: NEXUS_VERSION,
                protocol: NEXUS_PROTOCOL,
                nexusUrl: NEXUS_URL,
                groupId: 'tn.esprit',
                version: version,
                repository: repository,
                credentialsId: NEXUS_CREDENTIAL_ID,
                artifacts: [
                    [artifactId: 'tpFoyer-17', classifier: '', file: artifactFile, type: 'jar']
                ]
            )
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
