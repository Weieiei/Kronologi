# Appointment Scheduler

[![Build Status](https://travis-ci.com/vartanbeno/AppointmentScheduler.svg?token=246smhzQ1xhQqHvZsnxy&branch=master)](https://travis-ci.com/vartanbeno/AppointmentScheduler)

SOEN 490 (Capstone) project for Concordia University.

# Running the stack

First, clone the repository and run the following:

```
(
    cd server/src/main/resources
    cp secret.example.properties secret.properties
)
```

Then assign appropriate values to the variables defined in the `secret.properties` file.

## Docker

1. Make sure you have [docker](https://docs.docker.com/install/#supported-platforms) installed.
2. Make sure you have [docker-compose](https://docs.docker.com/compose/install/#install-compose) installed.
3. From root directory, run `docker-compose up`.
    - you can add the `-d` or `--detach` option to run containers in the background.

You should now be able to connect to the following:

- application server: [http://localhost:80/](http://localhost:80/)
- express server: [http://localhost:3000/](http://localhost:3000/)
- postgres server: localhost:5432
