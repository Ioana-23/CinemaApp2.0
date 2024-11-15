CREATE FUNCTION generate_uuid()
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
DO $$
    DECLARE
        nr_linii INT;
        nr_coloane INT;
        nr_maxim_coloane INT := 10;
        nr_minim_coloane INT := 5;
        nr_maxim_linii INT := 7;
        nr_minim_linii INT := 5;
        nr_movie_halls INT := 5;
        seat_id INT;
        uuid_final INT;
        exista INT;
    BEGIN
        FOR row IN 1..nr_maxim_linii LOOP
            FOR seat IN 1..nr_maxim_coloane LOOP
                    INSERT INTO seats VALUES (DEFAULT, row, seat);
            END LOOP;
        END LOOP;

        FOR movie_hall IN 1..nr_movie_halls LOOP
            uuid_final := generate_uuid();
            SELECT COUNT(*) INTO exista  FROM movie_halls WHERE uuid = uuid_final;
            WHILE exista LOOP
                uuid_final := generate_uuid();
                SELECT COUNT(*) INTO exista  FROM movie_halls WHERE uuid = uuid_final;
            END LOOP;
            INSERT INTO movie_halls VALUES (movie_hall, uuid_final);

            SELECT INTO nr_linii floor(random() * (nr_maxim_linii-nr_minim_linii+1) + nr_minim_linii)::int;
            SELECT INTO nr_coloane floor(random() * (nr_maxim_coloane-nr_minim_coloane+1) + nr_minim_coloane)::int;

            FOR i IN 1..nr_linii LOOP
                FOR j IN 1..nr_coloane LOOP
--                         RAISE NOTICE 'movie_hall: % i: % j: %', movie_hall, i, j;
                        SELECT s.id INTO seat_id FROM seats s WHERE s.row_number = i AND s.seat_number = j;
                    INSERT INTO movie_hall_configuration VALUES (DEFAULT, movie_hall, seat_id);
                END LOOP;
            END LOOP;
        END LOOP;
    END;
$$

