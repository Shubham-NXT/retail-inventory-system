# ==============================================================================
# Multi-Stage Dockerfile for Spring Boot (Optimized for Render 512MB Free Tier)
# ==============================================================================

# --- Stage 1: Build the Application ---
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /app

# Copy Maven descriptor and source files
COPY pom.xml .
COPY src ./src

# Build production jar skipping tests for fast deployment
RUN mvn clean package -DskipTests

# --- Stage 2: Lightweight Runtime Environment ---
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Create a non-root system user for security
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copy the built jar from the builder stage
COPY --from=builder /app/target/*.jar app.jar

# Expose standard application port
EXPOSE 8080

# Configure JVM memory limits to comfortably fit within Render's 512MB RAM free tier
ENV JAVA_OPTS="-Xmx384m -Xms128m -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError"

# Launch Spring Boot application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
