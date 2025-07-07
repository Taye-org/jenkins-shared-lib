def buildDocker(imageName) {
    echo "Building Docker image: ${imageName}"
    sh "docker build -t ${imageName} ."
}

def securityScan(imageName) {
    echo "Running Snyk scan..."
    withCredentials([string(credentialsId: 'snyk-token', variable: 'SNYK_TOKEN')]) {
        sh """
            snyk auth \$SNYK_TOKEN
            snyk test --docker ${imageName} --file=Dockerfile --severity-threshold=high 
        """
    }

    echo " Running Trivy scan..."
    sh "trivy image ${imageName} || true"
}

def runTests() {
    echo " No unit tests found — skipping for now."
}

def pushDocker(imageName, credentialsId) {
    withCredentials([usernamePassword(credentialsId: credentialsId, usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
        sh """
            echo \$PASSWORD | docker login -u \$USERNAME --password-stdin
            docker push ${imageName}
        """
    }
}

def deploy(env) {
    echo "🚀 Simulated deploy to environment: ${env}"
    
}

