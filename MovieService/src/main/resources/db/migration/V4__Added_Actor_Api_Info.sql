ALTER TABLE actors
    ADD COLUMN gender VARCHAR(255) NOT NULL DEFAULT 'OTHER' CHECK (gender IN ('MALE','FEMALE', 'OTHER', 'NON_BINARY')) ;

UPDATE actors SET gender = 'FEMALE' WHERE name = 'Florence Pugh';
UPDATE actors SET gender = 'MALE' WHERE name = 'Andrew Garfield';