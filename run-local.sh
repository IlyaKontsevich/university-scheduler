#!/bin/sh
./gradlew clean build
docker-compose build --no-cache
docker-compose -f docker-compose.yml up -d --force-recreate scheduler "${@}"