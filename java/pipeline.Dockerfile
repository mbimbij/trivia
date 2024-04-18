#FROM eclipse-temurin:21-jdk-alpine as build
#WORKDIR /workspace/app
#
#COPY ../.git .git
#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#COPY src src
#
#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/extracted
#RUN java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

FROM eclipse-temurin:21-jdk-alpine
LABEL org.opencontainers.image.source=https://github.com/mbimbij/trivia
VOLUME /tmp
COPY target .
ARG EXTRACTED=/target/extracted
COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]