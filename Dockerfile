# Use Java 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy your JAR file
COPY build/libs/*.jar app.jar

# Expose port (optional for Render)
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]