FROM openjdk:17-alpine3.14

LABEL author="Sokolov Ivan"
LABEL email="ivan.sklvvv@yandex.ru"

ENV SERVER_PORT=8080
ARG JAR_FILE_NAME=files-manipulator-0.0.1-SNAPSHOT.jar

WORKDIR ./app
COPY ./target/$JAR_FILE_NAME ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "files-manipulator-0.0.1-SNAPSHOT.jar"]