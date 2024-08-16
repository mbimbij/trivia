Trivia Remix
======

# Introduction

This repository is a fork of the popular kata named Trivia. It first started as a regular solution of the kata, then 
turned into a web application built on top of it. 

Why ? Just because ! It was seen as a fun exercise to brush the rust off after months of relative inactivity, 
and a reason to get my hand on Angular and other tools or techniques. 

# Repository structure

```shell
.
├── *                           ignore any files and directory other than "java" or ".github"
├── .github                        
│   └── workflows
│      └── pipeline.yml         the Github Actions pipeline
└── java                        
    ├── pom.xml                 root pom, as the project is setup as a multi-module Maven project
    ├── backend                 backend of the app, defined as a Maven module
    ├── e2e-tests               e2e tests using Playwright-java, defined as a Maven module
    ├── frontend-angular        the frontend, using Angular 
    ├── infra                   Terraform scripts to setup the envs (empty for the moment) 
    ├── docs                    miscellaneous drawio files to support design, and screenshots used in the README
    ├── .run                    run configurations to run the backend and e2e tests
    ├── pipeline.Dockerfile     Dockerfile used in the pipeline
    ├── Makefile                targets to execute most if not all actions necessary for development and some more
    └── openapi.yaml            Golden Source API contract exposed by the backend, used by the frontend.

```

# Prerequisites

## Tools

- IntelliJ
- java (optional if using IJ provided JVM)
- maven (optional if using IJ provided maven)
- Node & npm
- Angular CLI v17
- make (optional if you are not interested in executing actions using Makefile targets)

## Configure .env file

Used to reference variables by both run configurations and Makefile targets.
Also used to store secrets for e2e test user.

Copy `template.env` to `.env`. Ask for the secrets and replace them in `.env`.

## Verify run configurations and makefile targets

Most actions described in the README should an IntelliJ run configuration, saved in `.run` directory and versioned in git. They should be preconfigured in your IDE.

Most actions should also have an associated `Makefile` target

Both rely on a properly configured `.env` file

# Frontend dependencies

1. dsds
2. 
2. Generate the openapi stubs with the Makefile target `generate-frontend-stubs-from-openapi`

# Run the app locally

There are several ways to run the application locally, we will refer them as "profiles"

## Profile "local-ide" - default & preferred

The most standard way used in day to day development. Although the name refers to "IDE", you can actually run the frontend, the backend or both in this profile using the CLI.

The frontend runs on port 4200 and the backend on port 8080

![](/java/docs/readme-images/4.1-profile-local-ide.jpg)

### Run the frontend

You can run the frontend using an IJ run configuration `run-frontend - local-ide`

![](/java/docs/readme-images/1.0-run-frontend-local-ide.jpg)

Or using the terminal

```shell
cd java/frontend-angular
ng serve
```

The frontend should start without issue and the output should look like the following:

![](/java/docs/readme-images/1.0.1-ng-serve-output.jpg)

### Run the backend

Select the run configuration `backend - local-ide`:

![](/java/docs/readme-images/1.1-run-configurations.jpg)

You should get an output as follows (here backend started in debug)

![](/java/docs/readme-images/1.2-debug-backend.jpg)

### Access the app via the browser

You can now access the app via the browser. Go to `localhost:4200`:

![](/java/docs/readme-images/1.3-browser.jpg)

Choose an authentication method and have fun with the app.

## Profile "local-ide-embedded"

The frontend is hosted by the backend. The frontend build artifacts are placed in `src/main/resources/static` of the backend.

The option `watch` is set to `true` so that there is live reload on the frontend AND the backend.

The goal of this profile is to have a setup similar to what will be deployed, but with fast feedback loops.

it is useful for example to verify Angular routes and Spring MVC routes do not step on each other's toes.

The application is accessed via port 8100

![](/java/docs/readme-images/4.2-profile-local-ide-embedded.jpg)

### Build the frontend

Using the Makefile target `build-frontend-local-ide-embedded`

Or the IntelliJ run config `build-frontend - local-ide-embedde`

### Run the backend

Using the Makefile target `run-backend-local-ide-embedded` or `debug-backend-local-ide-embedded`

### Access the app via the browser

You can now access the app via the browser. Go to `localhost:8100`:

## Profile "local-docker"

Similar to `local-ide-embedded`, but not ran through the IDE, and packaged into a docker image, ran locally. It's useful to verify for some specific configs and setups, for example the spring config location, other local resources paths, or the Dockerfile.

No live reload on this one

![](/java/docs/readme-images/4.3-profile-local-docker.jpg)

![](java/docs/readme-images/)


### Build the Docker image

Using the Makefile target `build-docker-local`

### Run the Docker image

Using the Makefile target `run-docker-local`

### Access the app via the browser

You can now access the app via the browser. Go to `localhost:8110`:


# Run the unit tests

## Run the frontend tests

Use the pre-registered run configuration `test frontend`

Or use the Makefile target `unittests-frontend`

everything should be green, you should obtain an output as following:

![](/java/docs/readme-images/2.2-ng-test-output.jpg)

## Run the backend tests

Use the pre-registered run configuration `ng test`

![](/java/docs/readme-images/2.3-backend-tests-run-config.jpg)

You should get an output as follows:

![](/java/docs/readme-images/2.4-backend-tests-run-config-output.jpg)

Or use the Makefile target `unittests-backend`

![](/java/docs/readme-images/2.5-backend-tests-cli-output.jpg)

![](java/docs/readme-images/)

# Run the e2e tests

The goal of these tests is to ensure beyond any doubt that the application does work. They run against a running instance of the app, whether locally or in an environment.

The way the app is configured to run called is called "profile". cf section [Run the app locally](#run-the-app-locally) for more info.

## Profile "local-ide"

the backend and the frontend are ran locally from the IDE or the CLI.

![](/java/docs/readme-images/3.0-profile-local-ide.jpg)

cf section [run the app locally - profile "local-ide"](#profile-local-ide---default--preferred) for instructions on how to run the app in that configuration

Select the run config `e2e-tests - local-ide` to run all tests.

You can also select a run config related to e2e tests targeting a specific "feature", in our case mapping to a page, of the application. 

![](/java/docs/readme-images/3.1-profile-local-ide-run-config.jpg)

At the moment, e2e tests are configured to use Chrome. A browser should open and run the tests. After around 2 minutes, the tests should end and everything should be green.

![](/java/docs/readme-images/3.2-profile-local-ide-run-config-output.jpg)


## Profile "local-ide-embedded"

the frontend is hosted by the backend.

![](/java/docs/readme-images/3.3-e2e-tests-profile-local-ide-embedded.jpg)

cf section [run the app locally - profile "local-ide-embedded"](#profile-local-ide-embedded) for more details and instructions to run this configuration.

Select the run config `e2e-tests - local-ide-embedded` to run all tests.

Just like for the `local-ide` profile, there are run configurations available for specific features / pages.

## Profile "local-docker"

Just like the configuration "local-ide-embedded", but packaged in a Docker image 

![](/java/docs/readme-images/3.4-e2e-tests-profile-local-docker.jpg)

cf section [run the app locally - profile "local-docker"](#profile-local-docker) for more details and instructions to run this configuration.

Just like for the other profiles described above, there are run configurations available for specific features / pages.

---

![](java/docs/readme-images/)

# Update the README

`README.md` is generated so don't update it directly. Update `README.template.md`, then run the Makefile target `generate-readme` 

# Wiki and other documentation resources

https://github.com/mbimbij/trivia/wiki