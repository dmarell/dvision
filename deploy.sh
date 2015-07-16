#!/bin/sh
version=$1
targetHost=$2
username=$3
deployDir=$4

# copy files to target host
scp dvision-server/target/dvision-server-${version}.jar ${username}@${targetHost}:${deployDir}/
scp dvision-server/target/Dockerfile ${username}@${targetHost}:${deployDir}/
scp dvision-server/target/docker-run.sh ${username}@${targetHost}:${deployDir}/

ssh -l ${username} ${targetHost} "cd ${deployDir}; bash docker-run.sh"

# Initialization on targetHost:
# $ sudo cp dvision-server.conf /etc/init
