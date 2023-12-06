FROM openjdk:8-jdk-alpine
COPY /target/eureka-server-*.jar app.jar
EXPOSE 8761
CMD ["java", "-jar", "app.jar"]