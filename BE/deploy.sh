#!/usr/bin/env bash
#ㄹㅇ찐WLs막
APP_NAME=shleeb/campersapp
REPOSITORY=/home/ubuntu/campers
WEB_NAME=campersapp

echo "> Check the currently running container"
CONTAINER_ID=$(docker ps -aqf "name=$WEB_NAME")
if [ -z "$CONTAINER_ID" ];
then
  echo "> No such container is running."
else
  echo "> Stop and remove container: $CONTAINER_ID"
  docker stop "$CONTAINER_ID"
  docker rm "$CONTAINER_ID"
fi

echo "> Remove previous Docker image"
docker rmi "$APP_NAME"

echo "> Build Docker image"
docker build -t "$APP_NAME" "$REPOSITORY"

echo "> Run the Docker container"
docker run -d -p 3000:8080 --name "$WEB_NAME" "$APP_NAME"
