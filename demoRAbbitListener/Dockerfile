FROM azul/zulu-openjdk-alpine
ARG JAR_FILE=target/*.jar
EXPOSE 8081
COPY ${JAR_FILE} /home/backend/backend.jar
COPY .. /home/backend
CMD ["java", "-jar", "/home/backend/backend.jar"]