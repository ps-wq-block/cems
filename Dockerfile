# Build Stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Fix Windows line endings and make executable
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw
# Download dependencies
RUN ./mvnw dependency:go-offline || true
# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests

# Run Stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]