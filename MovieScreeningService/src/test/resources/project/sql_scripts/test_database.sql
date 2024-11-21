CREATE DATABASE MovieScreeningServiceTestDatabase
GO
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

ALTER TABLE seats
    ADD CONSTRAINT seat_movie_halL_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_movie_halls_fk FOREIGN KEY (movie_hall_id) REFERENCES movie_halls (id);

ALTER TABLE movie_screenings
    ADD CONSTRAINT movie_screening_pk PRIMARY KEY (id);

CREATE OR REPLACE FUNCTION generate_uuid()
    RETURNS INT
    LANGUAGE plpgsql
AS
$$
    DECLARE
nr_cifre INT;
        cifra_curenta INT;
        uuid_final INT := 1;
BEGIN
SELECT INTO nr_cifre floor(random() * (6-4+1) + 4)::int;
FOR index IN 1..nr_cifre LOOP
SELECT INTO cifra_curenta floor(random() * 10 + 1)::int;
uuid_final := uuid_final * 10 + cifra_curenta;
END LOOP;
RETURN uuid_final;

END;
$$;

CREATE FUNCTION hardcode_movie_hall(nr_maxim_linii INT, nr_minim_linii INT, nr_maxim_coloane INT, nr_minim_coloane INT)
    RETURNS VOID
AS
    $$
DECLARE
nr_linii INT;
        nr_coloane INT;
BEGIN
INSERT INTO movie_halls VALUES (1, 1);

SELECT INTO nr_linii floor(random() * (nr_maxim_linii-nr_minim_linii+1) + nr_minim_linii)::int;
SELECT INTO nr_coloane floor(random() * (nr_maxim_coloane-nr_minim_coloane+1) + nr_minim_coloane)::int;

FOR i IN 1..nr_linii LOOP
                FOR j IN 1..nr_coloane LOOP
                        INSERT INTO seats VALUES (DEFAULT, i, j, 1, 1);
--                         RAISE NOTICE 'movie_hall: % i: % j: %', movie_hall, i, j;
END LOOP;
END LOOP;
END;
$$;

DO $$
    DECLARE
nr_linii INT;
        nr_coloane INT;
        nr_maxim_coloane INT := 10;
        nr_minim_coloane INT := 5;
        nr_maxim_linii INT := 7;
        nr_minim_linii INT := 5;
        nr_movie_halls INT := 5;
        movie_hall_uuid INT;
        seat_uuid INT;
        exista INT;
BEGIN

        PERFORM hardcode_movie_hall(nr_maxim_linii, nr_minim_linii, nr_maxim_coloane, nr_minim_coloane);

FOR movie_hall IN 2..nr_movie_halls LOOP
            movie_hall_uuid := generate_uuid();
SELECT COUNT(*) INTO exista  FROM movie_halls WHERE uuid = movie_hall_uuid;
WHILE exista LOOP
                movie_hall_uuid := generate_uuid();
SELECT COUNT(*) INTO exista  FROM movie_halls WHERE uuid = movie_hall_uuid;
END LOOP;
INSERT INTO movie_halls VALUES (movie_hall, movie_hall_uuid);

SELECT INTO nr_linii floor(random() * (nr_maxim_linii-nr_minim_linii+1) + nr_minim_linii)::int;
SELECT INTO nr_coloane floor(random() * (nr_maxim_coloane-nr_minim_coloane+1) + nr_minim_coloane)::int;

FOR i IN 1..nr_linii LOOP
                    FOR j IN 1..nr_coloane LOOP
                            seat_uuid := generate_uuid();
SELECT COUNT(*) INTO exista  FROM seats WHERE uuid = seat_uuid;
WHILE exista LOOP
                                    seat_uuid := generate_uuid();
SELECT COUNT(*) INTO exista  FROM seats WHERE uuid = seat_uuid;
END LOOP;
INSERT INTO seats VALUES (DEFAULT, i, j, movie_hall, seat_uuid);
END LOOP;
END LOOP;
END LOOP;
END;
$$

