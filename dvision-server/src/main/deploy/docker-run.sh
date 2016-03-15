#!/bin/bash
apiuser=$1
apipassword=$2
apibaseurl=$3
mountdir=$4
outdir=$5

set -ex
docker build -t dvision-server .
docker stop dvision-server 2> /dev/null || true
docker rm -f dvision-server || true
docker run -d --name dvision-server \
    -e APIUSER=$apiuser \
    -e APIPASSWORD=$apipassword \
    -e APIBASEURL=$apibaseurl \
    -e OUTDIR=$outdir \
    -p 14562:8080 \
    -v $mountdir:$outdir \
    dvision-server
