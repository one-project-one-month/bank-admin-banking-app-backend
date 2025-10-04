# Stage 1: Build the application
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/nrc-service-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 3002 9093
ENTRYPOINT ["java","-jar","app.jar"]