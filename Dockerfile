# Step 1: Use a build image
FROM eclipse-temurin:21-jdk AS builder

WORKDIR /app

# Copy everything (source code)
COPY . .

# Build the JAR using Gradle wrapper
RUN ./gradlew clean bootJar

# Step 2: Use a lightweight image to run the app
FROM eclipse-temurin:21-jdk

WORKDIR /app

# Copy the JAR from builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "app.jar"]