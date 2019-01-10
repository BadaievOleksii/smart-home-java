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
