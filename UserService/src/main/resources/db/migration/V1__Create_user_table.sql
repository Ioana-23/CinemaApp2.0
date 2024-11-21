CREATE TABLE users
(
    id SERIAL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    uuid INTEGER UNIQUE NOT NULL,
    user_role VARCHAR(255) NOT NULL CHECK ( user_role IN ('GUEST', 'USER', 'ADMIN') ),
    email VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL
);

ALTER TABLE users
    ADD CONSTRAINT users_pk PRIMARY KEY (id);