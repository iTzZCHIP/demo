pipeline {
  agent any

  stages {
    stage('Checkout') {
      steps {
        // Nutzt die im Job konfigurierte SCM-Quelle
        git 'https://github.com/iTzZCHIP/demo'
      }
    }

    stage('Build & Test') {
      steps {
        sh 'chmod +x mvnw || true'
        sh './mvnw -B -ntp clean verify'
      }
      post {
        always {
          junit 'target/surefire-reports/*.xml'
        }
      }
    }

    stage('Code Quality (SonarQube)') {
      steps {
        withSonarQubeEnv('sonarqube') {
          sh """
            ./mvnw -B -ntp sonar:sonar \
              -Dsonar.projectKey=your_project_key \
              -Dsonar.java.binaries=target/classes
          """
        }
      }
    }

    stage('Quality Gate') {
      steps {
        timeout(time: 10, unit: 'MINUTES') {
          waitForQualityGate abortPipeline: true
        }
      }
    }
  }
}