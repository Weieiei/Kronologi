CREATE EXTENSION IF NOT EXISTS CITEXT;
CREATE EXTENSION IF NOT EXISTS PGCRYPTO;


CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL
        CHECK (char_length(first_name) >= 2),
    last_name VARCHAR(255) NOT NULL
        CHECK (char_length(last_name) >= 2),
    email VARCHAR(255) NOT NULL
        CHECK (email !~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,3}$'),
    username CITEXT UNIQUE NOT NULL
        CHECK (char_length(username) >= 4 AND char_length(username) <= 30),
    password_hash VARCHAR(255) NOT NULL,
    join_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE OR REPLACE FUNCTION hash_password() RETURNS TRIGGER AS
$$
BEGIN
    IF ((SELECT LENGTH(NEW.password_hash) < 6) OR (SELECT LENGTH(NEW.password_hash) > 30))
    THEN RAISE EXCEPTION 'The password should be 6 to 30 characters long.';
    END IF;
    NEW.password_hash := crypt(NEW.password_hash, gen_salt('bf', 8));
    RETURN NEW;
END
$$
LANGUAGE plpgsql;


CREATE TRIGGER hash_password
BEFORE INSERT ON users
    FOR EACH ROW EXECUTE PROCEDURE hash_password();


CREATE TRIGGER hash_updated_password
BEFORE UPDATE ON users
    FOR EACH ROW
    WHEN (OLD.password_hash IS DISTINCT FROM NEW.password_hash)
    EXECUTE PROCEDURE hash_password();
