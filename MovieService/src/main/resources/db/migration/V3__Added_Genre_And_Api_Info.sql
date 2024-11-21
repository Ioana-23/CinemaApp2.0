CREATE TABLE genres
(id   SERIAL,
 name VARCHAR(255) NOT NULL,
 uuid INTEGER UNIQUE NOT NULL,
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
    ADD COLUMN adult BOOLEAN NOT NULL DEFAULT FALSE;
ALTER TABLE movies
    ADD COLUMN overview VARCHAR(355) NOT NULL DEFAULT '';
ALTER TABLE movies
    ADD COLUMN language VARCHAR(255) NOT NULL DEFAULT 'eng';
