CREATE TABLE reservations
(
    id SERIAL,
    user_id INTEGER NOT NULL,
    movie_screening_uuid INTEGER NOT NULL,
    uuid INTEGER UNIQUE NOT NULL
);

CREATE TABLE tickets
(
    id SERIAL,
    ticket_type VARCHAR(255) NOT NULL CHECK ( ticket_type IN ('ADULT', 'CHILD') ),
    seat_uuid INTEGER NOT NULL
);

CREATE TABLE reservation_tickets
(
    id             SERIAL,
    reservation_id INTEGER NOT NULL,
    ticket_id      INTEGER NOT NULL
);

ALTER TABLE reservations
    ADD CONSTRAINT user_fk FOREIGN KEY (user_id) REFERENCES users(id);

ALTER TABLE reservations
    ADD CONSTRAINT reservations_pk PRIMARY KEY (id);

ALTER TABLE tickets
    ADD CONSTRAINT tickets_pk PRIMARY KEY (id);

ALTER TABLE reservation_tickets
    ADD CONSTRAINT reservations_fk FOREIGN KEY (reservation_id) REFERENCES reservations (id);

ALTER TABLE reservation_tickets
    ADD CONSTRAINT reservation_tickets_fk FOREIGN KEY (ticket_id) REFERENCES tickets (id);

ALTER TABLE reservation_tickets
    ADD CONSTRAINT reservation_tickets_pk PRIMARY KEY (id);