# Auth Middleware

Backend for Frontend application that provide sign in page and redirects access token to resource servers.

## Configuration

### Environment Variables

You will need to define the environment variables below on your computer path or in IDE run configuration:

- **AUTH_MIDDLEWARE_HTTP_PORT:** Server port (e.g. 4000);
- **TASKS_API_URL:** Tasks API URL (it will be used to forward requests to Tasks API);
- **AUTH_MIDDLEWARE_OAUTH2_JWK_SET_URL:** JWT public keys URL (for example in Keycloak: http://localhost:8080/realms/test/protocol/openid-connect/certs);
- **AUTH_MIDDLEWARE_OAUTH2_JWT_ISSUER_URL:** JWT issuer URL (for example in Keycloak: http://localhost:8080/realms/test);
- **AUTH_MIDDLEWARE_OPENID_CLIENT_ID:** Confidential client ID (you can create a client of this type in Keycloak enabling **Client authentication** and remains only **Standard flow** checked);
- **AUTH_MIDDLEWARE_OPENID_CLIENT_SECRET:** Confidential client secret (you can create a client of this type in Keycloak enabling **Client authentication** and remains only **Standard flow** checked).

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

