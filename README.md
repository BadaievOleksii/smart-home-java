[![Build Status](https://travis-ci.com/Sornemus/gethatch-test.svg?token=AJb1oQq7v4PBBxC88Zf3&branch=master)](https://travis-ci.com/Sornemus/gethatch-test)


######################
`docker-compose up -d mqtt`
Start mqtt broker in detached mode

`docker-compose ps`
Check currently running containers

`docker-compose exec mqtt tail -f /mosquitto/log/mosquitto.log`
Tail logs of running mqtt service


######################

`./gradlew run`
Build & run application
