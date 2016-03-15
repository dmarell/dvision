#!/bin/bash
version=$1
targetHost=$2
username=$3
deployDir=$4
apiuser=$5
apipassword=$6
apibaseurl=$7
mountdir=$8
outdir=$9

# copy files to target host
scp dvision-server/target/dvision-server-${version}.jar ${username}@${targetHost}:${deployDir}/
scp dvision-server/target/Dockerfile ${username}@${targetHost}:${deployDir}/
scp dvision-server/target/docker-run.sh ${username}@${targetHost}:${deployDir}/

ssh -l ${username} ${targetHost} "cd ${deployDir}; \
    bash docker-run.sh $apiuser $apipassword $apibaseurl $mountdir $outdir"

# Initialization on targetHost:
# $ sudo cp dvision-server.conf /etc/init
