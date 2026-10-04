# Use lightweight Eclipse Temurin OpenJDK 21 runtime
FROM eclipse-temurin:21-jre-alpine

# Set working directory inside the container
WORKDIR /app

# Copy the Gradle generated jar into the container
COPY build/libs/*.jar app.jar

# Expose application port
EXPOSE 8081

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]
