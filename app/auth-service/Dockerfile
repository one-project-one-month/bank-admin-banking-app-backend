
# Stage 2: Create the final image
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/quarkus-app/ .

EXPOSE 9999

ENTRYPOINT ["java", "-jar", "quarkus-run.jar"]