FROM eclipse-temurin:17-jre
WORKDIR /app

COPY target/quarkus-app/ .

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]
