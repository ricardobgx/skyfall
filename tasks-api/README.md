# Tasks API

API to manage Skyfall tasks.

## Configuration

### Requirements

- JDK 17
- Docker >= 27.4.1

### Environment Variables

You will need to define the environment variables below on your computer path or in IDE run configuration:

- **TASKS_API_HTTP_PORT:** Server port (e.g. 4201);
- **TASKS_API_OAUTH2_JWT_ISSUER_URL:** JWT issuer URL (for example in Keycloak: http://192.168.2.0:4001/realms/skyfall);

## Running

### Development mode

To run the app in development mode you just need to run the command below on terminal in project root:

```shell
mvn clean spring-boot:run
```

### Build

To build the project you will need to run the command below on terminal in project root:

```shell
mvn clean package -DskipTests
```

### Production mode

After build the project Maven will generate a **.jar** file in the target folder, after that you just need to run it with the command below on terminal inside **target** folder:

```shell
java -jar tasks-api-1.0.jar
```

## DevOps

You also can run the application using Docker and Docker Compose.

### Docker

**Build image**

To build the application Docker image you just need to run the command below on your terminal in project root:

```shell
docker build -t ricardobgx/skyfall-tasks-api .
```

**Create container**

To create a container using the image built in the previous step you just need to run the command below on your terminal in project root:

```shell
docker run -d --name skyfall-tasks-api \
-p <External server port (e.g. 4201)>:<Internal server port (e.g. 4201)> \
-e "SKYFALL_TASKS_API_HTTP_PORT=<External port (e.g. 4201)>" \
-e "SKYFALL_TASKS_API_OAUTH2_JWT_ISSUER_URL=<OAuth2.0 JWT issuer URL>" \
ricardobgx/skyfall-tasks-api
```

**Stop container**

To stop the running container you just need to run the command below on your terminal in project root:

```shell
docker container stop skyfall-tasks-api
```

### Docker Compose

**Start application**

After set the environment variables in your computer path or creating a .env with them inside, you just need to run the command below on your terminal in project root:

```shell
docker compose up -d
```

**Stop application**

To stop the application you just need to run the command below on your terminal in project root:

```shell
docker compose down
```
