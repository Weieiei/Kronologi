# How to Build a Booking System

## Business Requiremements

- 5-6 employees.
- Admin should be able to set employees' available times, which are subject to change.
- Spa offers different services, with the possibility of each taking different amounts of time to complete.

## Minimum Viable Product

For now, we should consider the minimum viable product (MVP) to entail something along the lines of:

1. User picks date + time to book appointment.
2. That date + time combination, tied to the ID of said user, is stored in an `appointments` table in the database.
3. Once booked, that date + time combination cannot be booked by another user.

## Phases

The final product will be much more complex than what the beginning phases may suggest. Appointments will hold a start time, end time, will be tied to an employee and not just a client, etc.

### Phase 1

A user, any user, can make a reservation once logged in. Let's not worry about canceling an appointment, modifying an appointment, etc.

Users will be stored in a `users` table with the following attributes:

- `user_id`: an integer which will serve as the primary key for the table.
- `first_name`
- `last_name`
- `email`
- `username`
- `password`

Appointments will be stored in an `appointments` table with the following attributes:

- `appointment_id`: an integer which will serve as the primary key for the table.
- `user_id`: an integer which references a user in a separate table, the one who booked the appointment; the client.
- `time`: the date and time that the client chose to make the reservation. Will most likely be the toughest attribute to implement.

Phase 1 should include a rough database schema that we will be working with. Some team members have already contributed to the creation of the following schema, which will be implemented one way or another into the project:

![DB Schema](https://camo.githubusercontent.com/317978086211581ee8e14c3c98cecb2cbdcbaecc/68747470733a2f2f692e696d6775722e636f6d2f6c684879364d472e706e67)

In addition to all of the above, a simple user interface should be created to facilitate the creation of bookings. Luckily, the component library we're working with, Angular Material, has a [datepicker](https://material.angular.io/components/datepicker/overview) component that we can easily inject into our application. It should then suffice to let the user pick out a desired time, and confirm the booking.

### Phase 2

Some ideas...

- Introduce the idea of multiple employees. So if an appointment has been made by a user, another user can still book an appointment at the same time with another employee.
    - Employees have all of the same attributes as users.
- Introduce the idea of services. When booking an appointment, a user should choose the type of service they'd like to reserve for.
    - A service's attributes are `service_id`, `name`, `duration`, and possibly `price`.
- Let the client cancel an appointment.
- Let the employee cancel an appointment.
- Figure out separation of roles/permissions. Should user/employee/admin all have the same power?

### Phase 3, etc.

// TODO
