#!/bin/sh
#
# Arguments                        Example
# $1=version to deploy             124
# $2=hostname where to deploy      vs1
# $3=username on deployment host   vuser
# $4=deployment directory          /usr/local/dvision-server

ssh -l $3 $2 "cd $4; [ ! -f dvision-server.jar ] || mv -f dvision-server.jar dvision-server.jar.old"
scp dvision-server/target/dvision-server-$1.jar $3@$2:$4/dvision-server.jar
ssh -l $3 $2 "sudo service dvision-server restart"

# Initialization on deployment_host:
#  sudo bash -c "cat id_rsa-jenkins.pub >> ~/<username_on_deployment_host>/.ssh/authorized_keys"
#  sudo adduser <username_on_deployment_host> sudo