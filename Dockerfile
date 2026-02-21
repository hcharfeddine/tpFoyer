# Dockerfile for Spring Boot Application
# Multi-stage build to keep the image small

# Stage 1: Build
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
# Only copy src to keep it clean
COPY src ./src
# Skip tests for faster build in this example
RUN mvn clean package -DskipTests

# Stage 2: Run
FROM openjdk:17-jdk-slim
WORKDIR /app
# Correct the jar name to match pom.xml artifactId
COPY --from=build /app/target/tpFoyer-17-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
