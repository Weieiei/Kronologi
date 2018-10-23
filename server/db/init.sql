CREATE EXTENSION IF NOT EXISTS CITEXT;

CREATE TABLE IF NOT EXISTS users(
    user_id SERIAL PRIMARY KEY,
    first_name VARCHAR(255) NOT NULL
        CHECK (char_length(first_name) >= 2),
    last_name VARCHAR(255) NOT NULL
        CHECK (char_length(last_name) >= 2),
    email CITEXT UNIQUE NOT NULL
        CHECK (email ~* '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,3}$'),
    username CITEXT UNIQUE NOT NULL
        CHECK (char_length(username) >= 4 AND char_length(username) <= 30),
    password VARCHAR(255) NOT NULL,
    join_date TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);