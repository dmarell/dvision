#!/bin/bash
apiuser=$1
apipassword=$2
apibaseurl=$3

set -ex
docker build -t dvision-server dvision-server/target
docker stop dvision-server 2> /dev/null || true
docker rm -f dvision-server || true
docker run -d --name dvision-server \
    -e DVISION_APIUSER=$apiuser \
    -e DVISION_APIPASSWORD=$apipassword \
    -p 10111:8080 \
    dvision-server
