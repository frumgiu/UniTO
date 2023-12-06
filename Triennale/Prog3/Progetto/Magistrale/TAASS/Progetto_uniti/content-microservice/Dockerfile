FROM openjdk:8-jdk-alpine
COPY /target/content-microservice-*.jar app.jar
EXPOSE 8082
CMD ["java", "-jar", "app.jar"]