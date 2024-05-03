ARG BUILD_IMAGE=maven:3.9.6-eclipse-temurin-11
ARG RUNTIME_IMAGE=eclipse-temurin:11-jre-jammy

# Build stage
FROM ${BUILD_IMAGE} as builder
WORKDIR /app
COPY ./pom.xml ./
RUN mvn dependency:resolve-plugins dependency:go-offline -B
COPY ./src ./src
RUN mvn clean install -DskipTests=True

# Run stage
FROM ${RUNTIME_IMAGE} as runner
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/*.jar" ]
