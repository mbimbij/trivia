#FROM trion/ng-cli:17.3.7 AS npm-install
#
#WORKDIR /app
#COPY --chown=node:node --chmod=777 ./frontend-angular/angular.json ./frontend-angular/node_modules* /app/node_modules/
#COPY --chown=node:node ./frontend-angular/*.json /app/
#RUN npm install
#
#FROM trion/ng-cli:17.3.7 AS build-frontend
#ARG FRONTEND_BUILD_CONFIGURATION="development"
#WORKDIR /app
#COPY --from=npm-install /app/node_modules /app/node_modules
#COPY --chown=node:node ./frontend-angular/ /app
#ENV FRONTEND_BUILD_CONFIGURATION=$FRONTEND_BUILD_CONFIGURATION
#RUN ng build --configuration $FRONTEND_BUILD_CONFIGURATION

#FROM maven:3.9.7-amazoncorretto-21 AS build-backend
#COPY --from=build-frontend /app/dist/frontend-angular src/main/resources/static
#COPY ./pom.xml ./pom.xml
#COPY ./e2e-tests/pom.xml ./e2e-tests/pom.xml
#COPY ./backend ./backend
#COPY ./.git ./.git
#RUN mvn -pl backend clean verify

#FROM amazoncorretto:21-alpine
#ARG ALLOWED_ORIGINS="*"
#ENV APPLICATION_ALLOWED_ORIGINS=$ALLOWED_ORIGINS
#COPY --from=build-backend build/libs/todolist.jar todolist.jar
#ENTRYPOINT ["java","-jar","/todolist.jar"]

FROM amazoncorretto:21-alpine
VOLUME /tmp
ARG EXTRACTED=/backend/target/extracted
COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
COPY ${EXTRACTED}/application/ ./
ENTRYPOINT ["java","org.springframework.boot.loader.launch.JarLauncher"]