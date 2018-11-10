#!/bin/sh

# Wait for the Postgres db to start
# If it hasn't started, wait 2 seconds before checking again
bash -c 'while !</dev/tcp/$DB_HOST/5432; do sleep 2; done;'

cd ./src/

# Set up tables in db
(cd ./db/ && ../../node_modules/.bin/knex migrate:latest)

# Run the server
node app.js
