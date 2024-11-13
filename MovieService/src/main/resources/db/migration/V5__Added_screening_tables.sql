CREATE TABLE seats
(
    id SERIAL,
    row_number INTEGER,
    seat_number INTEGER
);

CREATE TABLE movie_halls
(
    id SERIAL,
    uuid INTEGER UNIQUE
);

CREATE TABLE movie_hall_configuration
(
    id SERIAL,
    movie_hall_id INTEGER,
    seat_id INTEGER
);

CREATE TABLE movie_screenings
(
    id            SERIAL,
    movie_id      INTEGER,
    date          date,
    time          time,
    movie_hall_id INTEGER,
    uuid INTEGER UNIQUE
);

ALTER TABLE movie_halls
    ADD CONSTRAINT movie_halls_pk PRIMARY KEY (id);

ALTER TABLE seats
    ADD CONSTRAINT seats_pk PRIMARY KEY (id);

ALTER TABLE movie_hall_configuration
    ADD CONSTRAINT movie_halls_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

ALTER TABLE movie_hall_configuration
    ADD CONSTRAINT seats_fk FOREIGN KEY (seat_id) references seats(id);

ALTER TABLE movie_hall_configuration
    ADD CONSTRAINT movie_hall_configuration_pk PRIMARY KEY (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_movie_screening_fk FOREIGN KEY (movie_id) REFERENCES movies (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_movie_halls_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_screening_pk PRIMARY KEY (id);