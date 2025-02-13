CREATE TABLE seats
(
    id SERIAL,
    row_number INTEGER,
    seat_number INTEGER,
    movie_hall_id INTEGER,
    uuid INTEGER,
    available BOOLEAN DEFAULT TRUE
);

CREATE TABLE movie_halls
(
    id SERIAL,
    uuid INTEGER UNIQUE
);

CREATE TABLE movie_screenings
(
    id            SERIAL,
    movie_uuid      INTEGER,
    date          date,
    time          time,
    movie_hall_id INTEGER,
    uuid INTEGER UNIQUE
);

ALTER TABLE movie_halls
    ADD CONSTRAINT movie_halls_pk PRIMARY KEY (id);

ALTER TABLE seats
    ADD CONSTRAINT seats_pk PRIMARY KEY (id);

ALTER TABLE seats
    ADD CONSTRAINT seat_movie_halL_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

-- ALTER TABLE movie_hall_configurations
--     ADD CONSTRAINT movie_halls_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);
--
-- ALTER TABLE movie_hall_configurations
--     ADD CONSTRAINT movie_hall_seats_fk FOREIGN KEY (seat_id) references seats(id);
--
-- ALTER TABLE movie_hall_configurations
--     ADD CONSTRAINT movie_hall_configuration_pk PRIMARY KEY (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_movie_halls_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_screening_pk PRIMARY KEY (id);