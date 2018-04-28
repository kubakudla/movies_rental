USE movies;

DROP TABLE IF EXISTS bonus_point;
DROP TABLE IF EXISTS rental;
DROP TABLE IF EXISTS customer;
DROP TABLE IF EXISTS movie;

CREATE TABLE customer (
  id           INTEGER      NOT NULL AUTO_INCREMENT,
  first_name   VARCHAR(255) NOT NULL,
  last_name    VARCHAR(255) NOT NULL,
  last_updated TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  data_insert  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE movie (
  id           INTEGER      NOT NULL AUTO_INCREMENT,
  name         VARCHAR(255) NOT NULL,
  video_type   VARCHAR(255) NOT NULL,
  last_updated TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  data_insert  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id)
);

CREATE TABLE rental (
  id              INTEGER        NOT NULL AUTO_INCREMENT,
  rent_date       DATE           NOT NULL,
  return_date     DATE,
  nb_of_days      INTEGER        NOT NULL,
  nb_of_days_late INTEGER,
  movie_id        INTEGER        NOT NULL,
  customer_id     INTEGER        NOT NULL,
  price           DECIMAL(19, 2) NOT NULL,
  surcharges      DECIMAL(19, 2),
  last_updated    TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  data_insert     TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_rental_movie FOREIGN KEY (movie_id) REFERENCES movie (id),
  CONSTRAINT fk_rental_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE bonus_point (
  id           INTEGER   NOT NULL AUTO_INCREMENT,
  points       INTEGER   NOT NULL DEFAULT 0,
  customer_id  INTEGER   NOT NULL,
  last_updated TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  data_insert  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_bonus_point_customer FOREIGN KEY (customer_id) REFERENCES customer (id)
);

INSERT INTO customer (id, first_name, last_name) VALUES (1, 'John', 'Smith');
INSERT INTO customer (id, first_name, last_name) VALUES (2, 'James', 'Bond');
INSERT INTO customer (id, first_name, last_name) VALUES (3, 'Billy', 'Jean');

INSERT INTO movie (id, name, video_type) VALUES (1, 'Titanic', 'NEW_RELEASE');
INSERT INTO movie (id, name, video_type) VALUES (2, 'Spider man', 'REGULAR');
INSERT INTO movie (id, name, video_type) VALUES (3, 'Superman', 'NEW_RELEASE');
INSERT INTO movie (id, name, video_type) VALUES (4, 'Batman', 'OLD');
INSERT INTO movie (id, name, video_type) VALUES (5, 'Batman 2', 'REGULAR');
INSERT INTO movie (id, name, video_type) VALUES (6, 'Batman 3', 'NEW_RELEASE');
INSERT INTO movie (id, name, video_type) VALUES (7, 'Hero', 'REGULAR');