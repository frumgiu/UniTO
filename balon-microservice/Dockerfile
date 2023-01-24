FROM openjdk:8-jdk-alpine
COPY /target/balon-microservice-*.jar app.jar
EXPOSE 8085
CMD ["java", "-jar", "app.jar"]