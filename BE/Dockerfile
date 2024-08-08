# Use an official Gradle image to build the application
FROM gradle:7.6.4-jdk17 AS build

# Set the working directory in the container
WORKDIR /campers

# Copy the Gradle wrapper and build files
COPY gradlew ./
COPY gradle ./gradle
COPY build.gradle ./
COPY settings.gradle ./

# Ensure the Gradle wrapper has executable permissions
RUN chmod +x gradlew

# Copy the source code
COPY src ./src

# Download the dependencies and build the application
RUN gradle build --no-daemon -x test

# Use an official Amazon Corretto image to run the application
FROM amazoncorretto:17.0.11

# Set the working directory in the container
WORKDIR /campers

# Copy the built application from the build stage
COPY --from=build /campers/build/libs/campers-0.0.1-SNAPSHOT.jar /campers/campers-0.0.1-SNAPSHOT.jar

# Expose the port the application runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "campers-0.0.1-SNAPSHOT.jar"]
