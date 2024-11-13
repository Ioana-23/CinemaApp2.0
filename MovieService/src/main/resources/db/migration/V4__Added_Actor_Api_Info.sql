ALTER TABLE actors
    ADD COLUMN gender VARCHAR(255) CHECK (gender IN ('MALE','FEMALE', 'OTHER', 'NON_BINARY'));
ALTER TABLE actors
    ADD COLUMN uuid INTEGER UNIQUE;

UPDATE actors SET gender = 'FEMALE' WHERE name = 'Florence Pugh';
UPDATE actors SET gender = 'MALE' WHERE name = 'Andrew Garfield';
UPDATE actors SET uuid = 0 WHERE name = 'Florence Pugh';
UPDATE actors SET uuid = 1 WHERE name = 'Andrew Garfield';
