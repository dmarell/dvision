#!/bin/sh
#
# Arguments                        Example
# $1=version to deploy             124
# $2=hostname where to deploy      vs1
# $3=username on deployment host   vuser
# $4=deployment directory          /usr/local/dvision-server

ssh -l bv83 $2 "cd $4;mv -f dvision-server.jar dvision-server.jar.old"
scp dvision-server/target/dvision-server-$1.jar $3@$2:$4/dvision-server.jar
ssh -l bv83 $2 "sudo service dvision-server restart"


# Init på catalina7:
#  sudo bash -c "cat /usr/local/jenkins/id_rsa-djenkins.pub >> /home/bv83/.ssh/authorized_keys"
#  sudo adduser bv83 sudo