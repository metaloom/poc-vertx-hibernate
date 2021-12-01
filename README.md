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


## Issues


A few issues within the PoC still need solving. 

### Thread usage

Under load the `GET /users` endpoint fails:

```
Caused by: org.hibernate.HibernateException: java.lang.IllegalStateException: HR000069: Detected use of the reactive Session from a different Thread than the one which was used to open the reactive Session - this suggests an invalid integration; original thread: 'vert.x-eventloop-thread-4' current Thread: 'vert.x-eventloop-thread-13'
```

```bash
wrk -d 2000 -c 20  http://localhost:8888/users
```

### Closing of sessions

When exceeding concurrent requests on reading a user row the session manager throws `Session/EntityManager is closed` errors.

```
java.util.concurrent.CompletionException: java.lang.IllegalStateException: Session/EntityManager is closed
```

```bash
cat /proc/cpuinfo   | grep Core | wc -l
8
# OK
wrk -d 10s -c 16 http://localhost:8888/users/6e3f7a29-9c4c-4de5-b577-779f5b5f0328
# Failing
wrk -d 10s -c 20 http://localhost:8888/users/6e3f7a29-9c4c-4de5-b577-779f5b5f0328
```