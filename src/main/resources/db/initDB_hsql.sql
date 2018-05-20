DROP TABLE eats IF EXISTS;
DROP TABLE voices IF EXISTS;
DROP TABLE restaurants IF EXISTS;
DROP TABLE user_roles IF EXISTS;
DROP TABLE users IF EXISTS;
DROP SEQUENCE global_seq IF EXISTS;

CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

CREATE TABLE users
(
  id               INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name             VARCHAR(255)            NOT NULL,
  email            VARCHAR(255)            NOT NULL,
  password         VARCHAR(255)            NOT NULL
);
CREATE UNIQUE INDEX users_unique_email_idx ON USERS (email);

CREATE TABLE user_roles
(
  user_id INTEGER NOT NULL,
  role    VARCHAR(255),
  CONSTRAINT user_roles_idx UNIQUE (user_id, role),
  FOREIGN KEY (user_id) REFERENCES USERS (id) ON DELETE CASCADE
);

CREATE TABLE restaurants
(
  id          INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  name        VARCHAR(255)    NOT NULL
);

CREATE UNIQUE INDEX restaurant_unique_idx ON restaurants (name);

CREATE TABLE VOICES
(
  id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  user_id           INTEGER               NOT NULL ,
  restaurant_id     INTEGER               NOT NULL ,
  date              DATE                  NOT NULL,
  CONSTRAINT voices_idx UNIQUE (user_id, date),
  FOREIGN KEY (restaurant_id) REFERENCES RESTAURANTS (id) ON DELETE CASCADE
);
-- CREATE UNIQUE INDEX voices_idx ON voices (user_id, date);

CREATE TABLE eats
(
  id              INTEGER GENERATED BY DEFAULT AS SEQUENCE GLOBAL_SEQ PRIMARY KEY,
  date_time       DATE         NOT NULL,
  name            VARCHAR(255) NOT NULL,
  price           INT          NOT NULL,
  restaurant_id   INTEGER      NOT NULL,
  CONSTRAINT restaurant_eat_name_idx UNIQUE (restaurant_id, date_time, name),
  FOREIGN KEY (restaurant_id) REFERENCES restaurants (id) ON DELETE CASCADE
);
-- CREATE UNIQUE INDEX eat_name_unique_restaurant_idx ON eats (date_time, name, restaurant_id);