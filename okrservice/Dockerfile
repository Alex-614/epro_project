# Use an official Maven image as a base image
FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder

# Set the working directory
WORKDIR /app

# Copy only the necessary files for dependency resolution
COPY pom.xml .

# Download dependencies
RUN mvn dependency:go-offline

# Copy the entire project
COPY . .

# Build the application
RUN mvn package 

# Create the final image
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]