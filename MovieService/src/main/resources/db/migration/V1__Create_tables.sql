CREATE TABLE actors
(
    id   SERIAL,
    name VARCHAR(255) NOT NULL,
    uuid INTEGER UNIQUE NOT NULL,
    CONSTRAINT actors_pkey PRIMARY KEY (id)
);

CREATE TABLE movie_actors
(
    movie_id  INTEGER NOT NULL,
    actors_id INTEGER NOT NULL
);

CREATE TABLE movies
(
    id   SERIAL,
    date date NOT NULL,
    title VARCHAR(255) NOT NULL,
    uuid INTEGER UNIQUE NOT NULL,
    CONSTRAINT movies_pkey PRIMARY KEY (id)
);

ALTER TABLE movie_actors
    ADD CONSTRAINT actors_fk FOREIGN KEY (actors_id) REFERENCES actors (id) ON DELETE NO ACTION;

ALTER TABLE movie_actors
    ADD CONSTRAINT movie_actors_fk FOREIGN KEY (movie_id) REFERENCES movies (id) ON DELETE NO ACTION;

ALTER TABLE movie_actors
    ADD CONSTRAINT movie_actors_pk PRIMARY KEY (actors_id, movie_id)