# Authorization Server

Application to authenticate users.

## Configuration

### Requirements

- Docker >= 27.4.1

### Environment variables

You will need to define the environment variables below on your computer path or in IDE run configuration:

- **SKYFALL_AUTHORIZATION_SERVER_ADMIN_USER:** Admin username (e.g. admin);
- **SKYFALL_AUTHORIZATION_SERVER_ADMIN_PASS:** Admin password (e.g. admin);
- **SKYFALL_AUTHORIZATION_SERVER_URL:** Application URL (e.g. http://192.168.2.0:4001);
- **SKYFALL_AUTHORIZATION_SERVER_HTTP_PORT:** Application HTTP port (e.g. 4001).

### Import realm

You can import a realm configuration file creating a file with naming \*realm.json (e.g. test-realm.json) in project root, the application will import it automatically.

## DevOps

### Docker Compose

**Start application**

To start the entire application you just need to run the command below on terminal in project root:

```shell
docker compose up -d
```

**Stop application**

To stop the application run the command below on terminal in project root:

```shell
docker compose down
```
