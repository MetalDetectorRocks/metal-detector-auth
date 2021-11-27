![Continous Integration](https://github.com/MetalDetectorRocks/metal-detector-auth/workflows/Continous%20Integration/badge.svg)
[![codecov](https://codecov.io/gh/MetalDetectorRocks/metal-detector-auth/branch/master/graph/badge.svg)](https://codecov.io/gh/MetalDetectorRocks/metal-detector-auth)
![Docker Image](https://github.com/MetalDetectorRocks/metal-detector-auth/workflows/Docker%20Image/badge.svg)

![Alt](https://repobeats.axiom.co/api/embed/00997fe0c4bd71f2b8e6de452e532e38d2047663.svg "Repobeats analytics image")

<a name="introduction"></a>
## 1 Introduction
This repository contains the source code for the _Metal Detector Auth_ microservice. The service is a Kotlin based Spring Boot application.

It provides access tokens for the Metal Release Butler via OAuth2 client credentials flow.

<a name="download-source-code"></a>
## 2 Download source code

Clone the source code via:

```
git clone https://github.com/MetalDetectorRocks/metal-detector-auth.git
```

<a name="run-application-locally"></a>
## 3 Run application locally

To start the application locally, the following preparatory actions are necessary:

1. Install Java 17

2. Install Kotlin (v1.6.x)

3. Install Docker CE

4. Run `docker-compose up -d --no-recreate` from the root directory of the project. This starts a postgresql database that is needed locally to run the application.

5. Define the data source connection details in file `application.yml`:
    - `spring.datasource.username` (you have to use user `postgres`)
    - `spring.datasource.password` (password from `docker-compose.yml`)
    - `spring.datasource.url` (`jdbc:postgresql://localhost:5432/metal-detector-auth`, the database name must match `POSTGRES_DB` of service `auth-db` from `docker-compose.yml` file)
    - `security.private-key` (Private Key of KeyPair)
    - `security.public-key` (Public Key of KeyPair)

It is also possible to define all mentioned connection details and secrets as environment variables. In this case no variables in `application.yml` need to be changed. The names of the environment variables are already in the `application.yml` file. You can define the environment variables for example within a Run Configuration in IntelliJ (other IDEs have similar possibilities).

<a name="start-application"></a>
## 4 Start the application

via gradle
- Execute command `./gradlew bootRun` in root directory

via your IDE
- Execute main class `rocks.metaldetector.auth.MetalDetectorAuthApplication`

<a name="execute-tests-locally"></a>
## 5 Execute tests locally

via gradle
- Execute command `./gradlew clean test` in root directory

via your IDE
- Execute the task `test` from folder `verification`
- Please note: You might get the message "Test events were not received" if you do this via IntelliJ. This is intentional behaviour of gradle. If nothing changes in the tests themselves, they will not be executed repeatedly. If you still want to run the tests, you have to execute `clean` before.
