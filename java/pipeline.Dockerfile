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