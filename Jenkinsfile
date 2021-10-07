pipeline {

  agent  {
      label 'sdd'
  }
  options {
      timeout(time: 120, unit: 'MINUTES')
  }

//Utiliser Pipeline Utility Steps plugin pour lire des informations depuis pom.xml dans env variables

  environment {
      IMAGE = 'registry.tools.orange-sonatel.com/dif/selfcareb2c-accountmanagement'
      VERSION = readMavenPom().getVersion()
      NAME = readMavenPom().getArtifactId()
      PORT=8715
      ENV_REC = 'dsiselfcarebcorangeetmoi-rec'
      ENV_DEV = 'dsiselfcarebc-dev'
      SERVICE_NAME = "${ARTIFACT_ID}-db"

  }

  tools {
    maven "Maven_3.3.9"
  }

  stages {


     stage('Clean Package') {
        steps {
           sh 'mvn clean package'
           stash includes: 'target/*', name: 'target'
        }
     }




    stage('Build & Push Docker image') {
        agent  { label 'docker-builder' }
        options { skipDefaultCheckout() }
      steps {

          sh 'docker ps -qa -f name=${NAME} | xargs --no-run-if-empty docker rm -f'
          sh 'docker images -f reference=${IMAGE} -qa | xargs --no-run-if-empty docker rmi'
          sh 'rm -rf target/'

          unstash 'target'
          dir('target') {
            sh 'docker build -t ${IMAGE}:${VERSION}.b${BUILD_NUMBER} .'
            /* sh 'docker run --name=${NAME} -d --restart=always -e JAVA_OPTS="-Dspring.profiles.active=dev" --memory-reservation=256M --memory=512M -p ${PORT}:${PORT} ${IMAGE}:${VERSION}.b${BUILD_NUMBER}' */
            sh 'docker push ${IMAGE}:${VERSION}.b${BUILD_NUMBER}'
          }
      }
    }





            /*  ================ Mysql service DEV-REC ================================= */
    stage('Malaw DEV - Mysql service') {
            agent {label 'malaw-dev'}
              when {
                allOf {
     		        branch 'develop'
                   expression {
                  openshift.withCluster() {
                    openshift.withProject("${ENV_DEV}") {
                        return !openshift.selector("svc", "${SERVICE_NAME}").exists();
                    }
                  }
                }

                }

              }
              steps {
                //Generate maven-resource-plugin param files"
                sh 'mvn validate'
                script {
                  openshift.withCluster() {
                    openshift.withProject("${ENV_DEV}") {
                        //Process external-service-mysqld template for external mysql service
                        def models =  openshift.process( "openshift//external-service","--param-file=target/openshift/mysql-dev.params")

                        //Adding labels
                        for ( o in models ) {
                             o.metadata.labels[ "env" ] = "${ENV_DEV}"
                             o.metadata.labels[ "type" ] = "mysql-endpoint"
                             o.metadata.labels[ "app" ] = "${NAME}"
                             o.metadata.labels[ "from" ] = "jenkins-pipeline"
                        }

                        //Create objects processed
                         def created = openshift.apply( models )
                     }
                  }
                }
              }
            }

       stage('Malaw REC - Mysql service') {
                agent {label 'malaw-prod'}
                  when {
                    allOf {
     		        branch 'release'
                    expression {
                      openshift.withCluster() {
                        openshift.withProject("${ENV_REC}") {
                            return !openshift.selector("svc", "${NAME}").exists();
                        }
                      }
                    }
                    }
                  }
           steps {
                  //Generate maven-resource-plugin param files"
                  sh 'mvn validate'
                  script {
                    openshift.withCluster() {
                      openshift.withProject("${ENV_REC}") {
                          //Process external-service-mysqld template for external mysql service
                          def models =  openshift.process( "openshift//external-service","--param-file=target/openshift/mysql-rec.params")

                          //Adding labels
                          for ( o in models ) {
                               o.metadata.labels[ "env" ] = "${ENV_REC}"
                               o.metadata.labels[ "type" ] = "mysql-endpoint"
                               o.metadata.labels[ "app" ] = "${NAME}"
                               o.metadata.labels[ "from" ] = "jenkins-pipeline"
                          }
                          //Create objects processed
                          def created = openshift.apply( models )
                          }
                        }
                       }
                     }
            }

    /* ======================================  Fin Mysql service DEV-REC  ======================================== */

    /* ======================================  DEBUT Deploy DEV-REC  ======================================== */
                stage('Malaw DEV - Deploy') {
                    agent {label 'malaw-dev'}
                  when { branch 'SELFB2C-3734-deploy-dev'}
                      steps {
                        //Generate maven-resource-plugin param files"
                        sh 'mvn validate'
                        sh 'cat target/openshift/app-dev.params'
                        script {
                          openshift.withCluster() {
                            openshift.withProject("${ENV_DEV}") {
                                //Process spring-boot-image-docker-mysqldb template for app deployment
                                def models =  openshift.process( "openshift//spring-boot-docker-image-database","--param-file=target/openshift/app-dev.params")

                                //Adding labels
                                for ( o in models ) {
                                     o.metadata.labels[ "env" ] = "${ENV_DEV}"
                                     o.metadata.labels[ "type" ] = "spring-boot-app"
                                     o.metadata.labels[ "app" ] = "${NAME}"
                                     o.metadata.labels[ "from" ] = "jenkins-pipeline"
                                     o.metadata.labels[ "version" ] = "${VERSION}.b${BUILD_NUMBER}"
                                }

                                //Create objects processed
                                 def created = openshift.apply( models )
                             }
                          }
                        }
                      }
                }

            stage('Malaw REC - Deploy') {
                    agent {label 'malaw-prod'}
                    when { branch 'release'}

                      steps {
                        //Generate maven-resource-plugin param files"
                        sh 'mvn validate'
                        script {
                          openshift.withCluster() {
                            openshift.withProject("${ENV_REC}") {
                                //Process spring-boot-image-docker-mysqldb template for app deployment
                                def models =  openshift.process( "openshift//spring-boot-docker-image-database","--param-file=target/openshift/app-rec.params")

                                //Adding labels
                                for ( o in models ) {
                                     o.metadata.labels[ "env" ] = "${ENV_REC}"
                                     o.metadata.labels[ "type" ] = "spring-boot-app"
                                     o.metadata.labels[ "app" ] = "${NAME}"
                                     o.metadata.labels[ "from" ] = "jenkins-pipeline"
                                }
                                //Create objects processed
                                  def created = openshift.apply( models )
                                }
                            }
                         }
                       }
                     }

    /* ======================================  FIN Deploy DEV-REC  ======================================== */



    stage('Functionnals Tests Phases') {
      steps {
        parallel(
          "NR Tests": {
            echo 'Pas de tests pour le moment'

          },
          "Functionnals Tests": {
            echo 'Pas de tests pour le moment'

          }
        )
      }
    }





/*
    stage('Launch Qualys Scan') {
      steps {
        sh 'mvn qualys:scan'
      }
  }

    stage('Check Qualys Scan') {
      steps {
          script{
            timeout(time:90, unit: 'MINUTES'){
              waitUntil {
                sleep time: 7, unit: 'MINUTES'
                try{
                  sh 'mvn qualys:check'
                  def result = manager.logContains(".*SCAN-FINISHED*.")

                  if(result)
                    return true
                }catch(exc){
                  echo 'Une exception a été rencontrée... Retry en cours'
                }
                return false
              }
            }
          }
      }
  }

  stage('Analyse Qualys Report') {
      steps {
       script{
         try{
            sh 'mvn qualys:analyse-prepare'
          }catch(exc){
            echo 'Une exception a été rencontrée pendant qualys:analyse-prepare'
          }
         sleep time: 1, unit: 'MINUTES'
          try{
            sh 'mvn qualys:analyse-perform'
          }catch(exc){
            echo 'Une exception a été rencontrée pendant qualys:analyse-perform'
          }
          }
       }
   }

    stage('Qualys Download Report') {
       steps {
           sh 'mvn qualys:report'
           sleep time: 1, unit: 'MINUTES'
           sh 'mvn qualys:download'
       }
       post{
        success {
          archiveArtifacts artifacts: 'target/qualys/*.pdf'
		  emailext attachmentsPattern: 'target/qualys/*.pdf',
			body: 'Rapport Qualys joint au mail.',
			subject: '[QUALYS] Rapport Vulnerabilité',
			to: 'mouhamadoubambambacke.sow@orange-sonatel.com'
        }
       }
    }

 */


      stage('Release On Nexus') {
          when { anyOf { branch 'master' } }
          steps {
              echo 'Perform master'
              sh 'git checkout master'

              // Determines next version, updates poms and creates the commit
              // Add security in branchingModel, to only have semantic versions for releases/*
              sh  '''
                    ./mvnw -B -X -Pprod -Darguments="-DskipTests" release:clean release:prepare release:perform \
                        -DtagPattern=@{SYSTEM_COMPONENT}_@{VERSION}_@{DATE} \
                        -DbranchingModel=".*:@{VERSION}.@{BUILD}" \
                  '''
              stash includes: 'target/checkout/target/*', name: 'release'

          }
          post {
              success {
                  echo "[SUCCESS] New Version released !!!"
              }
          }
      }

      stage('Push Docker image') {
          when { anyOf { branch 'master' } }
          agent  { label 'docker-builder-dev' }
          options { skipDefaultCheckout() }
          steps {
              script {
                  def releaseVersion = "$VERSION".split('-')[0]
                  echo "release version is ${releaseVersion}"

                  unstash 'release'

                  dir('target/checkout/target') {
                      sh "docker build -t ${IMAGE}:${releaseVersion} ."
                      sh "docker push ${IMAGE}:${releaseVersion}"
                  }
              }
          }
           post {
                  always {
                      echo "[ALWAYS] Clean directory !!!"
                      deleteDir()
                        }
                }
      }


      }

  post {

     changed {
      emailext attachLog: true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT',  to: 'Team.selfcare-b2c@orange-sonatel.com'
   }
    failure {
      emailext attachLog: true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT',  to: 'Team.selfcare-b2c@orange-sonatel.com'
   }
    always {
      echo "[ALWAYS] Clean directory !!!"
      cleanWs()
      }

  }
}
