FROM ubuntu:latest
LABEL authors="Oluwaseyi.Fashina"
# ========== BUILD STAGE ==========
FROM maven:3.9-eclipse-temurin-17 AS builder

# Set working directory
WORKDIR /app

# First copy only the pom to leverage Docker layer caching
COPY pom.xml .

# Download dependencies (will be cached unless pom.xml changes)
RUN mvn dependency:go-offline -B

# Now copy the rest of the source code
COPY src ./src

# Package the app (skip tests for faster builds; remove -DskipTests in prod CI if you want)
RUN mvn clean package -DskipTests

# ========== RUNTIME STAGE ==========
FROM eclipse-temurin:17-jre-alpine

# Set working directory
WORKDIR /app

# Copy jar from builder
COPY --from=builder /app/target/*.jar app.jar

# Expose application port (adjust if not 8080)
EXPOSE 8080

# Entry point for the container
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]