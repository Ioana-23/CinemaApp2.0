CREATE TABLE users
(
    id SERIAL,
    first_name VARCHAR(255),
    last_name VARCHAR(255),
    uuid INTEGER UNIQUE,
    user_role VARCHAR(255) CHECK ( user_role IN ('GUEST', 'USER', 'ADMIN') ),
    email VARCHAR(255),
    password VARCHAR(255)
);

ALTER TABLE users
    ADD CONSTRAINT users_pk PRIMARY KEY (id);