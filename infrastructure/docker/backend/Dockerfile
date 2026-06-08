FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY gradlew .
COPY gradlew gradle
COPY builder.gradle .
COPY settings.gradle .
COPY src src
RUN chmod +x gradlew
RUN ./gradlew bootJar -x test --no-deamon

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
copy --from=builder /app/builder/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]