FROM openjdk:17

COPY build/libs/*app.jar /application/app.jar

WORKDIR /application

ENTRYPOINT ["java","-jar","app.jar"]