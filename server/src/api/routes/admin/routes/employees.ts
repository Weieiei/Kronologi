import express from 'express';
import { Connection } from '../../../../db/knex';
import * as bcrypt from 'bcrypt-nodejs';
import { Logger } from '../../../../models/logger';
import { Employee } from '../../../../models/user/Employee';
import { validatePassword } from '../../../../helpers';

const employees = express.Router();

const logger = Logger.Instance.getGrayLog();
const saltRounds = 10;

const knex = new Connection().knex();

/**
 * @route       POST api/admin/employees/register
 * @description Create a user of type employee.
 * @access      Private
 */
employees.post('/register', (req, res) => {

    const { firstName, lastName, email, username, password } = req.body;

    const pw = validatePassword(password);
    if (!pw['isValid']) return res.status(400).send({ error: pw['errors'] });

    const employee: Employee = new Employee(firstName, lastName, email, username, password);

    bcrypt.genSalt(saltRounds, (err, salt) => {

        bcrypt.hash(employee.getPassword(), salt, undefined, (err, hash) => {

            if (err) {
                console.log(err);
                logger.error('error with bcrypt', { errorData: err });
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            employee.setPassword(hash);

            knex.table('users').insert({
                first_name: employee.getFirstName(),
                last_name:  employee.getLastName(),
                email: employee.getEmail(),
                username: employee.getUsername(),
                password: employee.getPassword(),
                user_type: Employee.getType()
            })
                .then(result => {

                    return res.status(200).send({ message: 'Successfully registered an employee.' });

                })
                .catch(error => {

                    switch (error.constraint) {
                        case 'users_first_name_length':
                        case 'users_last_name_length':
                            return res.status(400).send({ nameError: 'Both first and last names should be at least 2 characters long.' });
                        case 'users_email_unique':
                            return res.status(400).send({ emailError: 'An account with this email already exists.' });
                        case 'users_username_unique':
                            return res.status(400).send({ usernameError: 'This username is taken.' });
                        case 'users_username_length':
                            return res.status(400).send({ usernameError: 'Usernames should be between 4 and 30 characters.' });
                    }

                    logger.error('employee registration failed', { errorData: error }, { employeeId: employee.getId() });
                    return res.status(500).send({ error });

                });

        });

    });

});

module.exports = employees;
