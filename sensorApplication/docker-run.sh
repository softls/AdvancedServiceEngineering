#!/bin/bash

function die() {
	echo "$@" >&2
	exit 1
}

export IMAGE_NAME=lenaskarlat/sensor-app
export CONTAINER_NAME=lenaskarlat_sensorapp
export PORT=34006
export clientEndpoint=a3kjvw1kd2ijg5.iot.us-west-2.amazonaws.com
export clientId=positioningApp
export certificateFile=/certs/positioning_app-certificate.pem.crt
export privateKeyFile=/certs/positioning_app-private.pem.key
export topics=sensors/sensor_A1,sensors/sensor_A2
export influxEndpoint=http://ec2-54-187-68-156.us-west-2.compute.amazonaws.com:8086/
export influxUser=superadmin
export influxPassword=superadmin
export databaseName=PositioningData 

docker images | grep "$IMAGE_NAME" >/dev/null 2>&1 || die "docker image '$IMAGE_NAME' not found -- run docker-build.sh first"
docker rm -f "$CONTAINER_NAME" || echo "(nothing killed)"

DOCKER_CMD="docker run"
DOCKER_CMD="$DOCKER_CMD -e clientEndpoint=$clientEndpoint"
DOCKER_CMD="$DOCKER_CMD -e clientId=$clientId"
DOCKER_CMD="$DOCKER_CMD -e certificateFile=$certificateFile"
DOCKER_CMD="$DOCKER_CMD -e privateKeyFile=$privateKeyFile"
DOCKER_CMD="$DOCKER_CMD -e topics=$topics"
DOCKER_CMD="$DOCKER_CMD -e influxEndpoint=$influxEndpoint"
DOCKER_CMD="$DOCKER_CMD -e influxUser=$influxUser"
DOCKER_CMD="$DOCKER_CMD -e influxPassword=$influxPassword"
DOCKER_CMD="$DOCKER_CMD -e databaseName=$databaseName"

DOCKER_CMD="$DOCKER_CMD -p $PORT:8080"
DOCKER_CMD="$DOCKER_CMD -v /home/ec2-user/certs:/certs"
DOCKER_CMD="$DOCKER_CMD --name=$CONTAINER_NAME"
#DOCKER_CMD="$DOCKER_CMD --rm"
DOCKER_CMD="$DOCKER_CMD $IMAGE_NAME"

echo
echo "Running docker with default settings: $DOCKER_CMD"
echo

$DOCKER_CMD

echo
echo "docker exited with status: $?"
echo


