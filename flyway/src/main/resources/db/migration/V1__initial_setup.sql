
/*
Enable UUID V4 Support
*/
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "user" (
  "uuid" uuid DEFAULT uuid_generate_v4 (),
  "username" varchar UNIQUE NOT NULL,
  "firstname" varchar,
  "lastname" varchar,
  "passwordhash" varchar,
  "email" varchar,
  "enabled" boolean DEFAULT true,
  "meta" varchar,
  "created" timestamp DEFAULT (now()),
  "creator_uuid" uuid,
  "edited" timestamp DEFAULT (now()),
  "editor_uuid" uuid,
  PRIMARY KEY ("uuid")
);

CREATE TABLE "group" (
  "uuid" uuid DEFAULT uuid_generate_v4 (),
  "name" varchar UNIQUE NOT NULL,
  "meta" varchar,
  "created" timestamp DEFAULT (now()),
  "creator_uuid" uuid,
  "edited" timestamp DEFAULT (now()),
  "editor_uuid" uuid,
  PRIMARY KEY ("uuid")
);

CREATE TABLE "user_group" (
  "user_uuid" uuid NOT NULL,
  "group_uuid" uuid NOT NULL,
  PRIMARY KEY ("user_uuid", "group_uuid")
);

ALTER TABLE "user" ADD FOREIGN KEY ("creator_uuid") REFERENCES "user" ("uuid");
ALTER TABLE "user" ADD FOREIGN KEY ("editor_uuid") REFERENCES "user" ("uuid");
ALTER TABLE "group" ADD FOREIGN KEY ("creator_uuid") REFERENCES "user" ("uuid");
ALTER TABLE "group" ADD FOREIGN KEY ("editor_uuid") REFERENCES "user" ("uuid");
ALTER TABLE "user_group" ADD FOREIGN KEY ("user_uuid") REFERENCES "user" ("uuid");
ALTER TABLE "user_group" ADD FOREIGN KEY ("group_uuid") REFERENCES "group" ("uuid");

CREATE UNIQUE INDEX ON "user" ("username");
CREATE UNIQUE INDEX ON "group" ("name");
