
/*
Enable UUID V4 Support
*/
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE "users" (
  "uuid" uuid DEFAULT uuid_generate_v4 (),
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

CREATE TABLE "groups" (
  "uuid" uuid DEFAULT uuid_generate_v4 (),
  "name" varchar UNIQUE NOT NULL,
  "meta" varchar,
  "created" timestamp DEFAULT (now()),
  "creator_uuid" uuid,
  "edited" timestamp DEFAULT (now()),
  "editor_uuid" uuid,
  PRIMARY KEY ("uuid")
);

CREATE TABLE "users_groups" (
  "user_uuid" uuid NOT NULL,
  "group_uuid" uuid NOT NULL,
  PRIMARY KEY ("user_uuid", "group_uuid")
);

ALTER TABLE "users" ADD FOREIGN KEY ("creator_uuid") REFERENCES "users" ("uuid");
ALTER TABLE "users" ADD FOREIGN KEY ("editor_uuid") REFERENCES "users" ("uuid");
ALTER TABLE "groups" ADD FOREIGN KEY ("creator_uuid") REFERENCES "users" ("uuid");
ALTER TABLE "groups" ADD FOREIGN KEY ("editor_uuid") REFERENCES "users" ("uuid");
ALTER TABLE "users_groups" ADD FOREIGN KEY ("user_uuid") REFERENCES "users" ("uuid");
ALTER TABLE "users_groups" ADD FOREIGN KEY ("group_uuid") REFERENCES "groups" ("uuid");

CREATE UNIQUE INDEX ON "groups" ("name");

