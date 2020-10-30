pipeline {
    agent any
    environment {
   
  DOCKER_TAG = getversion()
SERVER_ID='central'
      
  }
    
    stages{
             stage ('SCM Checkout') {  
                steps {  
                    echo 'Running build phase...'  
                   git 'file://///home/cicd/Downloads/Travelagent'
                }  
            } 


            stage ('Build') {  
                steps {  
                    echo 'Running build phase...'  
                    sh "mvn clean package"
script{
  dir('test'){
  sh 'touch $WORKSPACE/Artifact_$BUILD_NUMBER'
}

}               
 }  
            }  


 stage('Upload'){
steps{
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

}

}

stage('Publish build info'){

steps{


rtPublishBuildInfo(
buildName: JOB_NAME,
buildNumber: BUILD_NUMBER,
serverId:SERVER_ID

)
}
}

stage('Add Interactive Promotion')
{

steps{

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
rtAddInteractivePromotion(
serverId:SERVER_ID,
buildName: JOB_NAME,
buildNumber: BUILD_NUMBER

)


}
}
  

            stage ('Test') {  
                steps {  
                   echo 'Running build phase...'  
                   withSonarQubeEnv('scan') {
                sh 'mvn  sonar:sonar'
              }
                }   
            }
              stage ('Docker Build') 
            {  
                steps {  
                   sh 'docker build . -t travelagent:${DOCKER_TAG} '
                   // sh 'docker build . -t vivpra/travelagent:${DOCKER_TAG}'
              }
                    
                }
                   stage ('Ansible Deploy Push') 
            {  
                steps {  
                  
                  ansiblePlaybook credentialsId: 'cicd', disableHostKeyChecking: true, extras: "-e DOCKER_TAG=${DOCKER_TAG}", installation: 'ansible', inventory: 'dev.in', playbook: 'ansiblejenkins.yml' 
                }   
                }
                
            
              
              
            
           
    }
}


 def getversion(){
      def commitver=sh label: '', returnStdout: true, script: 'git rev-parse --short HEAD'
       return commitver
   }


