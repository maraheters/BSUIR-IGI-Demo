# Stage 1: Build the application
FROM jelastic/maven:3.9.5-openjdk-21 AS build

WORKDIR /app

COPY pom.xml .
COPY src/ ./src

RUN --mount=type=cache,target=/root/.m2,rw mvn clean package -DskipTests

# Stage 2: Create a smaller image with the built JAR
FROM bellsoft/liberica-runtime-container:jre-21-musl
WORKDIR /app
COPY --from=build /app/target/SparkplugBackend-0.0.1-SNAPSHOT.jar /opt/SparkplugBackend/SparkplugBackend-0.0.1-SNAPSHOT.jar

# Specify a startup command
# Also creates a jdwp connection for remote debugging
CMD ["java","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000", "-jar", "/opt/SparkplugBackend/SparkplugBackend-0.0.1-SNAPSHOT.jar"]