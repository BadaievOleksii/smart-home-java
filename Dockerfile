FROM maven:3.6-alpine

VOLUME /tmp
WORKDIR /tmp

COPY src src
COPY pom.xml .

RUN mvn package

RUN java -jar /tmp/target/gethatch-test-0.1.0.jar

