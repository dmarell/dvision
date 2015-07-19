#!/bin/bash
set -ex
docker build -t dvision-server .
docker stop dvision-server 2> /dev/null || true
docker rm -f dvision-server || true
docker run -d --name dvision-server \
    -p 14562:8080 \
    -v /mnt/raid/public/cams/cam-motion-grabber:/cam-motion-grabber \
    dvision-server
