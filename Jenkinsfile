pipeline {
    agent any
    environment {
   
  DOCKER_TAG = getversion()
SERVER_ID='central'
      
  }
    
    stages{
                      stage ('Build') {  
                steps {  
                    echo 'Running build phase...'  
                    sh "mvn clean "
script{
  dir('test'){
  sh 'touch $WORKSPACE/Artifact_$BUILD_NUMBER'
}

}               
 }  
            }  
   stage ('Testing and packaging') {  
                steps {  
                   echo 'Running build phase...'  
                   withSonarQubeEnv('scan') {
                sh 'mvn package sonar:sonar'
              }
                }   
            }

 stage('Artifactory'){


steps("Upload")
{

  rtUpload(
buildName: JOB_NAME,
buildNumber: BUILD_NUMBER,
serverId:SERVER_ID,
spec: '''{
  "files":[
{
"pattern":"/var/lib/jenkins/workspace/Poject-pipeline2/target/*.war*",
"target": "Poject-pipeline2/",
"recursive": "false"

}


]

}'''

)

rtPublishBuildInfo(
buildName: JOB_NAME,
buildNumber: BUILD_NUMBER,
serverId:SERVER_ID

)

rtAddInteractivePromotion(
serverId:SERVER_ID,
targetRepo: 'Poject-pipeline2/',
displayName:'Play artifacts',
buildName: JOB_NAME,
buildNumber: BUILD_NUMBER,
comment: 'hello',
sourceRepo: 'Poject-pipeline2/',
status: 'Released',
includeDependencies: true,
failFast: true,
copy: true
)

}



}
         
              stage ('Docker Build') 
            {  
                steps {  
                   sh 'docker build . -t travelagent:${DOCKER_TAG} '
                   // sh 'docker build . -t vivpra/travelagent:${DOCKER_TAG}'
              }
                    
                }
                   stage ('Depoly and Monitor') 
            {  
                steps {  
                  
                    
                sh 'docker container stop $(docker container ls -q  --filter  name=monitor*)'
                    sh 'docker rm --force monitor'
                  ansiblePlaybook credentialsId: 'cicd', disableHostKeyChecking: true, extras: "-e DOCKER_TAG=${DOCKER_TAG}", installation: 'ansible', inventory: 'dev.in', playbook: 'ansiblejenkins.yml' 
                
                
                }   
                }
                
            
              
              
            
           
    }
}


 def getversion(){
      def commitver=sh label: '', returnStdout: true, script: 'git rev-parse --short HEAD'
       return commitver
   }

