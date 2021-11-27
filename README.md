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
