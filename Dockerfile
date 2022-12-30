FROM openjdk:8-jdk-alpine
COPY /target/demoTelegramAPI-*.jar app.jar
EXPOSE 8083
CMD ["java", "-jar", "app.jar"]