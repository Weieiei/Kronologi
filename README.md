# Appointment Scheduler (Kronologi)


[![Build Status](https://travis-ci.com/Weieiei/Kronologi.svg?token=7N9eDY6SkjyrqRyfMw8A&branch=master)](https://travis-ci.com/Weieiei/Kronologi)


This repository is a copy of Appoitment Scheduler's state on February 10th and has the same commit history and branches as in the AppointmentScheduler as of that date.

We decided to all have our features working with the new backend which is the latest commit on master on the day the copy of AppointmentScheduler was made (this is the [commit](https://github.com/Weieiei/Kronologi/commit/4d926bec80065ec38cbba15c33591b12090c60fe)) .
Since some people already had their feature working with the backend change, and  we would be using this backend for the Android application, we think it would be more efficient to update everyone with the new backend and tag our release 2 with it. 

The PR's in this repository are linked to the issues in the AppointmentScheduler repository (links to the specific issues are provided in each of the PR's in this repository)

SOEN 490 (Capstone) project for Concordia University.
# Clone the repository and set up
After cloning, add a new file called secret.properties in the Kronologi/server/src/main/resources/
Copy paste the follow code to the newly created file.

twilio.account.sid=ACb13e39c6d0fc16a2900e5cf0e4a5a538<br/>
twilio.auth.token=84b8023cffb22f0a72c8cf2bbfe73459<br/>
twilio.from.number=+14387951801<br/>

#emailService properties<br/>
emailService.id=kronologi-appointments@outlook.com<br/>
emailService.password=_S7)CqNVJP}E(sE6<br/>

#cloudflare API key:<br/>
cloudflare.key=48a6b7eb7adf124a7388131b1daf01580e6df<br/>
cloudflare.email=marc.leclair0113@gmail.com<br/>

#Google API key:<br/>
google.key =AIzaSyC8dJm42p5ASWP_9_w-_1kkrV9PTj2L1rA<br/>


# Running the angular for the frontend
cd to client folder, do follow scripts:<br/>
npm insatll <br/>
ng servce<br/>
This will run  http://localhost:4200<br/>

# Running the backend with intelliJ
Import as a Maven project<br/>
Build the project and run <br/>

# Continuous Integration
running with travis, and the builds can be review here:
[![Build Status](https://travis-ci.com/Weieiei/Kronologi.svg?token=7N9eDY6SkjyrqRyfMw8A&branch=master)](https://travis-ci.com/Weieiei/Kronologi)

# Our documentation
Our document for iteration 13 can be found at the following link: https://docs.google.com/document/d/179TRykE3Y_d6OMDAfY3hynwP09fs0jRQNxlVFvOiBGc/edit#

The slide presentation can be found here:
 https://docs.google.com/presentation/d/1pr50xdgorZldymK19Dqg-xEYTaxGTOGofH2yz7rdsjQ/edit?ts=5cbd014f#slide=id.p1

We have also made a user manual here:
 https://docs.google.com/document/d/1unQv-PCBmX-QfEAzjvSi2UB498eqYPW6iWzCaQy05eg/edit#
Our poster can be seen here, in PNG format: https://drive.google.com/open?id=1VuSax9HUA8D_ezCFySQSSKIFQV_driMm

We have also made a survey, as discussed, to have data about our users, the results of which are visible here:
 https://docs.google.com/forms/d/1V0nEoUfld02Bh1opOYuEjeDYcGcLpzPHe_d9qMHZRoU/edit#responses
(to view the results, make sure you are logged in with your Google account).
Please let us know if there are any issues or if you or the tutors have trouble accessing any of the links.


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
