-include .env
SHELL := /bin/bash
.PHONY: *

prepare-frontend: generate-frontend-environment-files npm-install-frontend generate-openapi-from-backend

npm-install-frontend:
	cd frontend-angular/ && npm install

mvn-install:
	./mvnw clean install -Dmaven.test.skip=true --no-transfer-progress  -Dserver.port=$(PORT_OPENAPI_MAVEN_GENERATION)

generate-openapi-from-backend:
	./mvnw -pl backend -Dmaven.test.skip=true -Dserver.port=$(PORT_OPENAPI_MAVEN_GENERATION) verify

generate-frontend-environment-file-production:
	envsubst < frontend-angular/src/environments/environment.production.template.ts > frontend-angular/src/environments/environment.production.ts

generate-frontend-environment-files-local:
	envsubst < frontend-angular/src/environments/environment.local-ide.template.ts > frontend-angular/src/environments/environment.local-ide.ts
	envsubst < frontend-angular/src/environments/environment.local-ide-embedded.template.ts > frontend-angular/src/environments/environment.local-ide-embedded.ts
	envsubst < frontend-angular/src/environments/environment.local-docker.template.ts > frontend-angular/src/environments/environment.local-docker.ts

generate-frontend-environment-files: generate-frontend-environment-files-local generate-frontend-environment-file-production

generate-frontend-stubs-from-openapi:
	cd frontend-angular/ && ./generate-from-openapi.sh

generate-readme:
	cd .. && envsubst < README.template.md > README.md

build-frontend-local-ide-embedded: generate-frontend-environment-files npm-install-frontend
	cd frontend-angular/ && ng build -c local-ide-embedded

build-frontend-local-docker: generate-frontend-environment-files npm-install-frontend
	cd frontend-angular/ && ng build -c local-docker

build-backend:
	./mvnw -pl backend package  -DskipTests -DskipITs
	java -Djarmode=layertools -jar backend/target/*.jar extract --destination backend/target/extracted

build-docker-local: build-frontend-local-docker build-backend
	docker build -f pipeline.Dockerfile -t cless91/trivia:local .

unittests-frontend: generate-frontend-environment-files npm-install-frontend
	cd frontend-angular/ && ng test

unittests-backend:
	./mvnw -pl backend clean verify

run-frontend-local-ide: generate-frontend-environment-files npm-install-frontend
	cd frontend-angular/ && ng serve

run-backend-local-ide:
	APPLICATION_QUESTIONS_PATH=src/main/resources/questions ./mvnw -pl backend spring-boot:run -Dserver.port=$(PORT_LOCAL_IDE_BACKEND)

run-backend-local-ide-embedded:
	APPLICATION_QUESTIONS_PATH=src/main/resources/questions ./mvnw -pl backend spring-boot:run -Dserver.port=$(PORT_LOCAL_IDE_EMBEDDED)

run-docker-local:
	docker run -p $(PORT_LOCAL_DOCKER):$(PORT_LOCAL_DOCKER) -e SPRING_PROFILES_ACTIVE=local-docker cless91/trivia:local

debug-backend-local-ide:
	APPLICATION_QUESTIONS_PATH=src/main/resources/questions ./mvnw -pl backend spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$(PORT_LOCAL_IDE_DEBUG_BACKEND)"

debug-backend-local-ide-embedded:
	APPLICATION_QUESTIONS_PATH=src/main/resources/questions ./mvnw -pl backend spring-boot:run -Dserver.port=$(PORT_LOCAL_IDE_EMBEDDED) \
-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$(PORT_LOCAL_IDE_EMBEDDED_DEBUG_BACKEND)"

e2e-tests-local-ide:
	./mvnw -pl e2e-tests test -Dtest="CucumberRunnerTest" -Dspring.profiles.include=local-ide

e2e-tests-local-ide-embedded:
	./mvnw -pl e2e-tests test -Dtest="CucumberRunnerTest" -Dspring.profiles.include=local-ide-embedded

e2e-tests-local-docker:
	./mvnw -pl e2e-tests test -Dtest="CucumberRunnerTest" -Dspring.profiles.include=local-docker

e2e-tests-prod:
	./mvnw -pl e2e-tests test -Dsurefire.includeJUnit5Engines=cucumber -Dspring.profiles.include=prod -Dcucumber.plugin=pretty -Dcucumber.features=src/test/resources/features/games-list.feature:9
	#./mvnw -pl e2e-tests clean test -Dsurefire.includeJUnit5Engines=cucumber -Dspring.profiles.include=prod -Dcucumber.features=/home/joseph/workspace/trivia/java/e2e-tests/src/test/resources/features/game-list.feature:9
