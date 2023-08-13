# Use the official Amazon Corretto Maven-based image from Docker Hub for the build stage
FROM eclipse-temurin:17 AS build

# Set the working directory in the Docker container
WORKDIR /app

# Copy the pom.xml and source code into the container
COPY ../../pom.xml ./
COPY ../../src ./src/

# Install Maven
RUN apt-get update && apt-get install -y maven

# Package the application
#RUN mvn package -DskipTests
RUN mvn package

# Use the official Amazon Corretto image for runtime
FROM eclipse-temurin:17

# Set the working directory in the Docker container
WORKDIR /app

# Copy the jar file from the build stage into the current stage
COPY --from=build /app/target/onlinebookstore-0.0.1-SNAPSHOT.jar /app/onlinebookstore.jar

# Expose port 8080 for the application
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "onlinebookstore.jar"]
