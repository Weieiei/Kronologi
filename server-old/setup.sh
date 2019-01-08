#!/bin/sh

# Wait for the Postgres db to start
# If it hasn't started, wait 2 seconds before checking again
# Do this 10 times max, we don't want the program to hang forever

# If a connection exists, set up tables, populate with data, and start the server

bash -c '
    limit=10
    counter=0

    while !</dev/tcp/$DB_HOST/5432
    do
        if [ $counter -eq $limit ]
        then
            echo "A database connection could not be established."
            exit 1
        fi

        counter=$(($counter+1))
        sleep 2
    done

    if </dev/tcp/$DB_HOST/5432 ;
    then
        (
            cd ./dist/db/
            npx knex migrate:latest
            npx knex seed:run
        )

        node ./dist/app.js
    fi
'
