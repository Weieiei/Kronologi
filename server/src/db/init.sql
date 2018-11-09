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

CREATE TABLE IF NOT EXISTS employees(
    employee_id SERIAL PRIMARY KEY,
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

CREATE TABLE IF NOT EXISTS appointments(
	appointment_id SERIAL PRIMARY KEY,
	user_id SERIAL REFERENCES users(user_id),
	employee_id SERIAL REFERENCES employees(employee_id),
  service_id SERIAL REFERENCES services(service_id),
	start_time TIMESTAMP NOT NULL,
	end_time TIMESTAMP NOT NULL,
	notes VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS services(
  service_id SERIAL PRIMARY KEY,
  duration_in_hours INT,
  price FLOAT,
  description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS businesses(
  business_id SERIAL PRIMARY KEY,
  name VARCHAR (255) NOT NULL,
  address VARCHAR (255)
);

CREATE TABLE IF NOT EXISTS employee_hours(
  employee_id SERIAL REFERENCES employee(employee_id),
  start_time TIMESTAMP NOT NULL,
  end_time TIMESTAMP NOT NULL,
  PRIMARY KEY (employee_id, start_time)
);
