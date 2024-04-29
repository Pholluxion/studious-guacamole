# Core

This is the core of the project. It contains the main logic of the project.

## Run the build"

```bash
./gradlew clean build
```

## Run the application

```bash
./gradlew clean bootRun
```

## Create a docker image

```bash
docker build -t pholluxion/core:latest .
```

## Run the docker image

```bash
docker run -p 8080:8080 pholluxion/core:latest
```

## Push the docker image

```bash
docker push pholluxion/core:latest
```

## Up the docker compose

```bash
docker compose up
```

## Down the docker compose

```bash
docker compose down
```
