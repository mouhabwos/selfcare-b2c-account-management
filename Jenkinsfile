pipeline {

  agent  {
      label 'sdd'
  }
  options {
      timeout(time: 120, unit: 'MINUTES')
  }

//Utiliser Pipeline Utility Steps plugin pour lire des informations depuis pom.xml dans env variables

  environment {
    
    	EMAIL_RECIPIENTS = 'Team.selfcare-b2c@orange-sonatel.com;cheikhahmettidjane.sankare@orange-sonatel.com'  
    	//PROFILE = getProfileFromBranch(env.BRANCH_NAME)
    	//DEST_ENV = getEnvFromBranch(env.BRANCH_NAME)
    	//AGENT = getAgentFromBranch(env.BRANCH_NAME)
    	IMAGE = "registry.tools.orange-sonatel.com/dif/${ARTIFACT_ID}"
      	VERSION = readMavenPom().getVersion()
      	NAME = readMavenPom().getArtifactId()
      	ARTIFACT_ID = readMavenPom().getArtifactId()
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



    stage('Units Tests') {
      steps {
        sh 'mvn clean test -Dmaven.test.skip=false'
      }
      post {
        success {
          junit 'target/surefire-reports/**/*.xml'
        }

      }

    }


     stage('SonarQube Scan') {
       steps{
         script{
             withSonarQubeEnv('SonarQubeServer') {
             sh 'mvn sonar:sonar -X'
            }
        }
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



         stage("SonarQube Quality Gate") {
          steps{
              script{
                timeout(time: 10, unit: 'MINUTES') {
                   sleep 5
                   def qg = waitForQualityGate()
                   if (qg.status != 'OK') {
                     error "Pipeline aborted due to quality gate failure: ${qg.status}"
                   }
                }

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
                  when { branch 'develop'}
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
    
    
    
    // Continious Deployement
 stage('Deploy New Release on Preproduction Namespace') {
     when { anyOf { branch 'master' } }
     agent  { label 'malaw4-prod' }
     options { skipDefaultCheckout() }
     steps {
         script {
             def releaseVersion = "$VERSION".split('-')[0]
             echo "release version is ${releaseVersion}"
             build job: 'ocd-selfcare-b2c-pprod/master',parameters: [[$class: 'StringParameterValue', name: 'msName', value: "$ARTIFACT_ID"], [$class: 'StringParameterValue', name: 'msVersion', value: "$releaseVersion"]]
         }
     }
 }
    
 
  }


      

  post {

     changed {
      emailext attachLog: true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT',  to: '${EMAIL_RECIPIENTS}'
   }
    failure {
      emailext attachLog: true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT',  to: '${EMAIL_RECIPIENTS}'
   }
    always {
      echo "[ALWAYS] Clean directory !!!"
      cleanWs()
      }

  }
}


def notifyTeam() {
    emailext attachLog: true, body: '$DEFAULT_CONTENT', subject: '$DEFAULT_SUBJECT',  to: "$EMAIL_RECIPIENTS"
}

def static getProfileFromBranch(branch) {
    if (branch == 'staging') {
        return 'rec'
    } else {
        return 'dev'
    }
}

def static getEnvFromBranch(branch) {
    if (branch == 'staging') {
        return 'dsiapimanagement-rec'
    } else {
        return 'dacdifdsapimanagement-dev'
    }
}

def static getAgentFromBranch(branch) {
    if (branch == 'staging') {
        return 'malaw-prod'
    } else {
        return 'malaw-dev'
    }
}
