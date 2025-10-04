# Stage 1: Build the application
FROM maven:3.8.7-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/quarkus-app/ .    

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]