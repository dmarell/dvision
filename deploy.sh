#!/bin/sh
#
# Arguments                        Example
# $1=version to deploy             124
# $2=hostname where to deploy      vs1
# $3=username on deployment host   vuser
# $4=deployment directory          /usr/local/dvision-server

ssh -l bv83 $2 "cd $4;mv -f dvision-server.jar dvision-server.jar.old"
scp target/dvision-server-$1.jar $3@$2:$4/dvision-server.jar

#ssh -i <identity_file> -l <sudo_username> $2 "sudo service dvision-server restart"
