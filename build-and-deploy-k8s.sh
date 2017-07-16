#!/usr/bin/env bash
set -e
version=2.2.8
mvn versions:set -DnewVersion=${version}
mvn clean package
bash deploy-k8s.sh ${version}
#bash deploy.sh ${version} 192.168.100.144
