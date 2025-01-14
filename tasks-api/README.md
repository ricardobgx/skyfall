# Tasks API

API to manage Skyfall tasks.

## Configuration

### Environment Variables

You will need to define the environment variables below on your computer path or in IDE run configuration:

- **TASKS_API_HTTP_PORT:** Server port (e.g. 4001);
- **TASKS_API_OAUTH2_JWT_ISSUER_URL:** JWT issuer URL (for example in Keycloak: http://localhost:8080/realms/test);

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

