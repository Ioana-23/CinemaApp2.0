CREATE TABLE genres
(id   SERIAL,
 name VARCHAR(255),
 uuid INTEGER UNIQUE,
 CONSTRAINT genres_pkey PRIMARY KEY (id)
);

CREATE TABLE movie_genres
(
    movie_id  INTEGER NOT NULL,
    genre_id INTEGER NOT NULL
);
ALTER TABLE movie_genres
    ADD CONSTRAINT genres_fk FOREIGN KEY (genre_id) REFERENCES genres (id) ON DELETE NO ACTION;

ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_fk FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE NO ACTION;

ALTER TABLE movie_genres
    ADD CONSTRAINT movie_genres_pk PRIMARY KEY (genre_id, movie_id);

ALTER TABLE movies
    ADD COLUMN adult BOOLEAN;
ALTER TABLE movies
    ADD COLUMN overview VARCHAR(355);
ALTER TABLE movies
    ADD COLUMN language VARCHAR(255);
ALTER TABLE movies
    ADD COLUMN uuid INTEGER UNIQUE;

UPDATE movies SET adult = false;
UPDATE movies SET overview = '';
UPDATE movies SET language = 'en';
UPDATE movies SET uuid = 0
