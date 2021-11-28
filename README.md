# Vert.x Hibernate Reactive PoC

This PoC project contains a Vert.x Hibernate Reactive setup.

It consists of the following components:

| Module      | Description                                                      |
|-------------|------------------------------------------------------------------|
| bom         | Bill Of Materials POM which tracks all dependency versions.      |
| api         | API Module which contains interfaces for the DAO implementation. |
| io-uring    | Vert.x Patches which will enable io uring support in Netty.      |
| flyway      | Flyway database migration helper.                                |
| hibernate   | Hibernate module which contains the reactive DAO implementations |
| rest        | Vert.x REST API implementation                                   |
| server      | Vert.x Http Server runner which provides the REST API            |

## Requirements

* Maven 3.6.3
* JDK 16
* Docker Runtime
* Linux Environment (otherwise io uring can be used)

## Building

```bash
export JAVA_HOME=<YOUR JDK 16>
mvn clean package -DskipTests
```

## REST API

* `POST /users` - Adds a new user to the database
* `GET /users` - Returns UUIDs of all users that have been created

## Demo

The server project contains the `Runner` class which can be used to start the demo server.
Alternatively the server can be started after building via:

```bash
java -jar server/target/poc-vertx-hibernate-server-0.0.1-SNAPSHOT.jar
```

## Docker

The server requires a postgres database. By default this database will be provides using `TestContainers` during startup of the demo application.
