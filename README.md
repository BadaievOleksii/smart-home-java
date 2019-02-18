# Smart home (Java)
   
[![Build Status](https://travis-ci.com/Sornemus/gethatch-test.svg?token=AJb1oQq7v4PBBxC88Zf3&branch=master)](https://travis-ci.com/Sornemus/gethatch-test)   
   
## ####################
### Technologies
- **Spring Boot 2** - application framework
- **Spring Integration** - for messaging with MQTT broker 
- **Gradle** - build tool
- **Docker Compose** - for working environment
- **Mosquitto** - MQTT-compliant message broker for IoT devices communication
   
## ####################
### Usage 
   
- Start mqtt broker in detached mode   
`docker-compose up -d mqtt`   
- Check currently running containers   
`docker-compose ps`  
- Tail logs of running mqtt service    
`docker-compose exec mqtt tail -f /mosquitto/log/mosquitto.log`  
- Build & run application    
`./gradlew run` 
