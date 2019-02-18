# Appointment Scheduler


[![Build Status](https://travis-ci.com/Weieiei/AppointmentSchedulerAndroid.svg?token=7N9eDY6SkjyrqRyfMw8A&branch=master)](https://travis-ci.com/Weieiei/AppointmentSchedulerAndroid)


This repository is a copy of Appoitment Scheduler's state on February 10th and has the same commit history and branches as in the AppointmentScheduler as of that date.

We decided to all have our features working with the new backend which is the latest commit on master on the day the copy of AppointmentScheduler was made (this is the commit : https://github.com/Weieiei/AppointmentSchedulerAndroid/commit/4d926bec80065ec38cbba15c33591b12090c60fe) .
Since some people already had their feature working with the backend change, and  we would be using this backend for the Android application, we think it would be more efficient to update everyone with the new backend and tag our release 2 with it. 

The PR's in this repository are linked to the issues in the AppointmentScheduler repository (links to the specific issues are provided in each of the PR's in this repository)

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
