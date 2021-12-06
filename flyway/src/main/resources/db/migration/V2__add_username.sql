
ALTER TABLE "users" 
    ADD "username" varchar(255) NOT NULL;

CREATE UNIQUE INDEX ON "users" ("username");


