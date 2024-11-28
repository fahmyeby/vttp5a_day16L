# base image with JDK to build and run java app
FROM maven:3.9.9-eclipse-temurin-23

ARG APP_DIR=/app

# directory where src code resides
WORKDIR ${APP_DIR}

# copy required files into the image
COPY mvnw .
COPY mvnw.cmd .
COPY pom.xml .
COPY .mvn .mvn
COPY src src

# package app using run directive
# this will download dependencies in pom.xml
# compile and package in jar
RUN mvn package -Dmaven.test.skip=true

# set server port 
ENV SERVER_PORT=8080

# expose port
EXPOSE ${SERVER_PORT}

# use entrypt to run the app
ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar target/day16practice-0.0.1-SNAPSHOT.jar
