FROM gradle:8.14.4-jdk17
WORKDIR /app

FROM openjdk:17-ea-17-slim
COPY /app/build/libs/*.jar   /app.jar
EXPOSE 8080
CMD ["java", "-jar", "/app.jar"]