FROM maven:3.8.7-eclipse-temurin-17 as build
WORKDIR /app
COPY pom.xml .
COPY src ./src

FROM eclipse-temurin:17-jre
COPY ./target/upload-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","app.jar"]
