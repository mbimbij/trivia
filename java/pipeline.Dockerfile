FROM amazoncorretto:21-alpine
LABEL org.opencontainers.image.source=https://github.com/mbimbij/trivia
VOLUME /tmp
ARG EXTRACTED=/backend/target/extracted
COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]