pipeline {
  agent {
    kubernetes {
      label "gradle-kaniko-${UUID.randomUUID().toString()}"
      defaultContainer 'gradle'
      yaml """
apiVersion: v1
kind: Pod
spec:
  restartPolicy: Never
  containers:
    - name: gradle
      image: gradle:8.9-jdk17
      command: ['cat']
      tty: true
      volumeMounts:
        - name: gradle-cache
          mountPath: /home/gradle/.gradle
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      command: ['/busybox/sh','-c','sleep infinity']
      tty: true
      volumeMounts:
        - name: docker-config
          mountPath: /kaniko/.docker
        - name: workspace
          mountPath: /workspace
  volumes:
    - name: gradle-cache
      emptyDir: {}
    - name: workspace
      emptyDir: {}
    - name: docker-config
      secret:
        secretName: dockerhub-cred
        items:
          - key: .dockerconfigjson
            path: config.json
"""
    }
  }

  environment {
    IMAGE_NAME = 'alswn00/backend'
    IMAGE_TAG  = "${env.BUILD_NUMBER}"
  }
  stages {
    stage('Checkout') {
      steps {
        container('gradle') {
          checkout([$class: 'GitSCM',
            branches: [[name: '*/main']],
            userRemoteConfigs: [[url: 'https://github.com/minju0077/k8s-be24-3rd-fisher-Facet.git']]
          ])
        }
      }
    }

    stage('Gradle Build') {
      steps {
        container('gradle') {
          sh 'pwd'
          sh 'ls -al'
          sh 'ls -al ..'
          sh '''
            chmod +x ./gradlew || true
            ./gradlew --no-daemon clean bootJar
          '''
        }
      }
    }
    stage('Kaniko Build & Push') {
      steps {
        container('kaniko') {
          sh """
            /kaniko/executor \
              --context=${WORKSPACE} \
              --dockerfile=${WORKSPACE}/Dockerfile \
              --destination=${IMAGE_NAME}:${IMAGE_TAG} \
              --single-snapshot \
              --use-new-run \
              --cache=true \
              --snapshotMode=redo
          """
        }
      }
    }

    stage('Result') {
      steps {
        echo "Pushed: ${IMAGE_NAME}:${IMAGE_TAG}"
      }
    }
    stage('Blue-Green Deploy') {
      steps {
        // 헬름 기본 에이전트인 jnlp 컨테이너 환경을 사용합니다.
        container('jnlp') {
          script {
            def buildNum = env.BUILD_ID.toInteger()
            def targetColor = (buildNum % 2 == 0) ? "green" : "blue"

            echo "현재 빌드 번호 ${buildNum}에 따라 ${targetColor} 배포를 시작합니다."

            // kubectl 명령어가 이제 정상 작동할 것입니다.
            sh "kubectl set image deployment/backend-${targetColor} backend=alswn00/backend:${buildNum}"
          }
        }
      }
    }

    stage('Service Traffic Shift') {
      steps {
        container('jnlp') {
          script {
            def buildNum = env.BUILD_ID.toInteger()
            def targetColor = (buildNum % 2 == 0) ? "green" : "blue"

            echo "서비스 트래픽을 ${targetColor}로 전환합니다."

            // 서비스의 셀렉터를 변경하여 트래픽 방향을 바꿉니다.
            sh "kubectl patch svc backend-service -p '{\"spec\":{\"selector\":{\"version\":\"${targetColor}\"}}}'"
          }
        }
      }
    }
  }
}