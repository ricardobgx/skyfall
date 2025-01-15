# Auth Middleware

Backend for Frontend application that provide sign in page and redirects access token to resource servers.

## Configuration

### Requirements

- JDK 17
- Docker >= 27.4.1

### Environment Variables

You will need to define the environment variables below on your computer path or in IDE run configuration:

- **SKYFALL_AUTH_MIDDLEWARE_HTTP_PORT:** Server port (e.g. 4101);
- **SKYFALL_TASKS_API_URL:** Tasks API URL (it will be used to forward requests to Tasks API);
- **SKYFALL_WEB_BASE_URL**: Web interface base URL;
- **SKYFALL_AUTH_MIDDLEWARE_OAUTH2_JWK_SET_URL:** JWT public keys URL (for example in Keycloak: http://192.168.2.0:4001/realms/skyfall/protocol/openid-connect/certs);
- **SKYFALL_AUTH_MIDDLEWARE_OAUTH2_JWT_ISSUER_URL:** JWT issuer URL (for example in Keycloak: http://192.168.2.0:4001/realms/skyfall);
- **SKYFALL_AUTH_MIDDLEWARE_OPENID_CLIENT_ID:** Confidential client ID (you can create a client of this type in Keycloak enabling **Client authentication** and remains only **Standard flow** checked);
- **SKYFALL_AUTH_MIDDLEWARE_OPENID_CLIENT_SECRET:** Confidential client secret (you can create a client of this type in Keycloak enabling **Client authentication** and remains only **Standard flow** checked).

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
java -jar auth-middleware-1.0.jar
```

## DevOps

You also can run the application using Docker and Docker Compose.

### Docker

**Build image**

To build the application Docker image you just need to run the command below on your terminal in project root:

```shell
docker build -t ricardobgx/skyfall-auth-middleware .
```

**Create container**

To create a container using the image built in the previous step you just need to run the command below on your terminal in project root:

```shell
docker run -d --name skyfall-auth-middleware \
-p <External server port (e.g. 4101)>:<Internal server port (e.g. 4101)> \
-e "SKYFALL_AUTH_MIDDLEWARE_HTTP_PORT=<External port (e.g. 4101)>" \
-e "SKYFALL_TASKS_API_URL=<Tasks API URL>" \
-e "SKYFALL_AUTH_MIDDLEWARE_OAUTH2_JWK_SET_URL=<OAuth2.0 JWK Set URL>" \
-e "SKYFALL_AUTH_MIDDLEWARE_OAUTH2_JWT_ISSUER_URL=<OAuth2.0 JWT issuer URL>" \
-e "SKYFALL_AUTH_MIDDLEWARE_OPENID_CLIENT_ID=<OpenID confidential client id>" \
-e "SKYFALL_AUTH_MIDDLEWARE_OPENID_CLIENT_SECRET=<OpenID confidential secret>" \
ricardobgx/skyfall-auth-middleware
```

**Stop container**

To stop the running container you just need to run the command below on your terminal in project root:

```shell
docker container stop skyfall-auth-middleware
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
