# Vert.x Hibernate Reactive PoC

This PoC project contains a Vert.x Hibernate Reactive setup.

It consists of the following components:

| Module      | Description                                                      |
|-------------|------------------------------------------------------------------|
| bom         | Bill Of Materials POM which tracks all dependency versions.      |
| api         | API Module which contains interfaces for the DAO implementation. |
| io-uring    | Vert.x Patches which will enable io uring support in Netty.      |
| flyway      | Flyway database migration helper. (Currently not setup)          |
| hibernate   | Hibernate module which contains the reactive DAO implementations |
| rest        | Vert.x REST API implementation                                   |
| server      | Vert.x Http Server runner which provides the REST API            |

## Requirements

* Maven 3.6.3
* JDK 16
* Docker Runtime
* Linux Environment (otherwise io uring can be used)

## Core Components

* Vert.x - 4.2.1
* Dagger - 2.39.1
* Hibernate Reactive - 1.1.0.Final
* RxJava - 3.1.3
* Netty - 4.1.69.Final
* Netty io uring - 0.0.10.Final

## Building

```bash
export JAVA_HOME=<YOUR JDK 16>
mvn clean package -DskipTests
```

## REST API

* `POST /users` - Adds a new user to the database
* `GET /users` - Returns UUIDs of all users that have been created
* `GET /users/:uuid` - Return a specific user
* `DELETE /users/:uuid` - Delete a specific user

## Demo

The server project contains the `io.metaloom.poc.Runner` class which can be used to start the demo server.
Alternatively the server can be started after building via:

```bash
java -jar server/target/poc-vertx-hibernate-server-0.0.1-SNAPSHOT.jar
```

## Architecture

1. The `Runner` class prepares the Dagger 2 dependency graph which is needed for dependency injection.
2. The `io.metaloom.poc.dagger.module` dagger modules prepare various components for DI that are outside of the project (e.g. Vert.x `VertxModule`, Hibernate `HibernateModule`, TestContainer `ContainerModule`)
3. The `RESTServerImpl` class deploys the `ServerVerticle` verticles which will be provided by dagger.
4. The `ServerVerticle` verticles startup a `HttpServer` and register the Vert.x routes for the REST API.
5. The handlers for the routes are located within the `GroupCrudHandler` and `UserCrudHandler` classes. The handler code utilizes RxJava3 and the Hibernate DAO implementation to interact with the database.

## Docker

The server requires a postgres database. By default this database will be provides using `TestContainers` during startup of the demo application.


## Open Task

* API Testing
* Logging Setup
* Open API handling
* Authentication?
* Metrics?

## Open Issues

A few issues within the PoC still need solving. 
