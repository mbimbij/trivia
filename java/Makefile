include .env
#SHELL := /bin/bash
.PHONY: *

generate-openapi-from-backend:
	cd backend/ && mvn -Dmaven.test.skip=true -Dserver.port=$(PORT_OPENAPI_MAVEN_GENERATION) verify

generate-frontend-stubs-from-openapi:
	cd frontend-angular/ && ./generate-from-openapi.sh

generate-readme:
	cd .. && envsubst < README.template.md > README.md

build-frontend-local-ide-embedded:
	cd frontend-angular/ && ng build -c local-ide-embedded

build-frontend-local-docker:
	cd frontend-angular/ && ng build -c local-docker

build-backend:
	cd backend/ && mvn package -Dmaven.test.skip=true
	java -Djarmode=layertools -jar backend/target/*.jar extract --destination backend/target/extracted

build-docker-local: build-frontend-local-docker build-backend
	docker build -f pipeline.Dockerfile -t cless91/trivia:local .

unittests-frontend:
	cd frontend-angular/ && ng test

unittests-backend:
	cd backend/ && mvn clean verify

run-frontend-local-ide:
	cd frontend-angular/ && ng serve

run-backend-local-ide:
	cd backend/ && mvn spring-boot:run

run-backend-local-ide-embedded:
	cd backend/ && mvn spring-boot:run -Dserver.port=$(PORT_LOCAL_IDE_EMBEDDED)

run-docker-local:
	docker run -p $(PORT_LOCAL_DOCKER):$(PORT_LOCAL_DOCKER) -e SPRING_PROFILES_ACTIVE=local-docker cless91/trivia:local

debug-backend-local-ide:
	mvn spring-boot:run -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$(PORT_LOCAL_IDE_DEBUG_BACKEND)"

debug-backend-local-ide-embedded:
	cd backend/ && mvn spring-boot:run -Dserver.port=$(PORT_LOCAL_IDE_EMBEDDED) \
-Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:$(PORT_LOCAL_IDE_EMBEDDED_DEBUG_BACKEND)"
