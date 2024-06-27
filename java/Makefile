.PHONY: *

all: frontend backend docker-image

frontend:
	VERSION=local && cd frontend-angular/ && ng build -c local-docker

backend:
	cd backend/ && mvn package -Dmaven.test.skip=true
	java -Djarmode=layertools -jar backend/target/*.jar extract --destination backend/target/extracted

docker-image:
	docker build -f pipeline.Dockerfile -t cless91/trivia:local .