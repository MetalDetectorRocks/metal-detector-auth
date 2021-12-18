![Continous Integration](https://github.com/MetalDetectorRocks/metal-detector-auth/workflows/Continous%20Integration/badge.svg)
[![codecov](https://codecov.io/gh/MetalDetectorRocks/metal-detector-auth/branch/master/graph/badge.svg)](https://codecov.io/gh/MetalDetectorRocks/metal-detector-auth)
![Docker Image](https://github.com/MetalDetectorRocks/metal-detector-auth/workflows/Docker%20Image/badge.svg)

![Alt](https://repobeats.axiom.co/api/embed/00997fe0c4bd71f2b8e6de452e532e38d2047663.svg "Repobeats analytics image")

## 1 Introduction

This repository contains the source code for the _Metal Detector Auth_ microservice. The service is a Kotlin based Spring Boot application.

It provides access tokens for the Metal Release Butler via OAuth2 client credentials flow.

## 2 Download source code

Clone the source code via:

```
git clone https://github.com/MetalDetectorRocks/metal-detector-auth.git
```

## 3 Run application locally

To start the application locally, the following preparatory actions are necessary:

1. Install Java 17

2. Install Kotlin (v1.6.x)

3. Install Docker CE

4. Run `docker-compose up -d --no-recreate` from the root directory of the project. This starts a postgres database that is needed locally to run the application.

5. Define the data source connection details in file `application.yml`:
    - `spring.datasource.username` (you have to use user `postgres`)
    - `spring.datasource.password` (password from `docker-compose.yml`)
    - `spring.datasource.url` (`jdbc:postgresql://localhost:5432/metal-detector-auth`, the database name must match `POSTGRES_DB` of service `auth-db` from `docker-compose.yml`)

6. Define the `client-id` and `client-secret` for oauth clients `metal-detector-user` and `metal-detector-admin`. Choose any value you want.

7. Deposit the private key and public key of your generated rsa key pair.
    - `security.authorization-server-private-key`
    - `security.authorization-server-public-key`

It is also possible to define all mentioned connection details and secrets as environment variables. In this case no variables in `application.yml` need to be changed. The names of the environment variables are already in the `application.yml` file. You can define the environment variables for example within a Run Configuration in IntelliJ (other IDEs have similar possibilities).

In local setup it is also possible to simply define empty environment variables with name `AUTHORIZATION_SERVER_PRIVATE_KEY` and `AUTHORIZATION_SERVER_PUBLIC_KEY`. The key pair is then generated automatically when the application is started.

## 4 Generate Key Pair manually

Generate an RSA key pair:

```
openssl genrsa -out keypair.pem 2048
```

Extract the public key:

```
openssl rsa -in keypair.pem -pubout -out public_key.crt
```

Extract the private key:

```
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private_key.key
```

From these files the actual keys have to be extracted without the first and last line (the `---` parts) and without the line breaks and put into the corresponding environment variable.

## 5 Start the application

via gradle
- Execute command `./gradlew bootRun` in root directory

via your IDE
- Execute main class `rocks.metaldetector.auth.MetalDetectorAuthApplication`

## 6 Execute tests locally

via gradle
- Execute command `./gradlew clean check` in root directory

via your IDE
- Execute the task `test` from folder `verification`
- Please note: You might get the message "Test events were not received" if you do this via IntelliJ. This is intentional behaviour of gradle. If nothing changes in the tests themselves, they will not be executed repeatedly. If you still want to run the tests, you have to execute `clean` before.
