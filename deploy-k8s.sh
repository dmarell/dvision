#!/bin/bash
set -e
if [ "$#" -ne 1 ]; then
    echo "Usage: $0 <version>"
    exit 1
fi

version=$1
projectPrefix='dvision'
namespace=${projectPrefix}-prod
clusterName='caglabs'
developersConsoleProject='caglabs-155116'
imageTag="eu.gcr.io/${developersConsoleProject}/${clusterName}-${projectPrefix}:${version}"
configurationEnv=prod
deployTimestamp=`date -u +'%Y-%m-%dT%H:%M:%SZ'`

rm -rf k8s.sed || true; cp -rf k8s/  k8s.sed/
sed -i.bak "s#PLACEHOLDER_IMAGE_TAG#${imageTag}#"                   k8s.sed/server.yaml
sed -i.bak "s#PLACEHOLDER_NODE_ENV#${configurationEnv}#"            k8s.sed/server.yaml
sed -i.bak "s#PLACEHOLDER_VERSION#\"${version}\"#"                  k8s.sed/server.yaml
sed -i.bak "s#PLACEHOLDER_DEPLOY_TIMESTAMP#\"${deployTimestamp}\"#" k8s.sed/server.yaml

docker build -t ${imageTag} dvision-server/target
gcloud docker -- push ${imageTag}
kubectl create namespace ${namespace} >& /dev/null || true
kubectl --namespace=${namespace} apply -f k8s.sed/