buildNumber=5
mvn versions:set -DnewVersion=${buildNumber} clean install
bash deploy-k8s.sh ${buildNumber}
#bash deploy.sh ${buildNumber} 192.168.100.144