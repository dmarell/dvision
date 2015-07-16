#!/bin/bash
set -ex
docker stop dvision-server || true
docker rm -f dvision-server || true
docker build -t dvision-server .
docker run -d --name dvision-server \
    -p 14562:8080 \
    -v $(mkdir -p logs;cd logs; pwd):/logs \
    -v /mnt/raid/public/cams/cam-motion-grabber:/cam-motion-grabber
    dvision-server
