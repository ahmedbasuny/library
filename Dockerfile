# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-slim

WORKDIR /app

COPY target/library.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
