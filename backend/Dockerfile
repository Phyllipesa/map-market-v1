# First stage
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /backend
COPY . .
RUN mvn clean package -DskipTests

# Secound stage
FROM eclipse-temurin:21-jdk
COPY --from=build /backend/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]