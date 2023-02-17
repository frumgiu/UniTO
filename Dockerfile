#FROM openjdk:8-jdk-alpine
FROM azul/zulu-openjdk-alpine
RUN apk add libssl3
RUN apk add libstdc++
#RUN apk add libssl-dev
ARG JAR_FILE=target/*.jar
EXPOSE 8083
ENV LD_LIBRARY_PATH=/home/backend/lib/:/usr/local/lib
COPY ${JAR_FILE} /home/backend/backend.jar
COPY . /home/backend
CMD ["java", "-Djava.library.path=/home/backend/lib/", "-jar", "/home/backend/backend.jar"]
