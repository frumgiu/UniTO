FROM openjdk:8-jdk-alpine
COPY /target/board-microservice-*.jar app.jar
EXPOSE 8084
CMD ["java", "-jar", "app.jar"]