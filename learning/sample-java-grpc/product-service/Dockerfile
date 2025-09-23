FROM maven:3.8.7-eclipse-temurin-17 as build

COPY src/main/proto ./src/main/proto

FROM eclipse-temurin:17-jre

COPY ./target/product-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8083 9092
ENTRYPOINT ["java","-jar","app.jar"]
