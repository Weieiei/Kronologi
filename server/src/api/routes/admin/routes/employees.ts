import express from 'express';
import * as bcrypt from 'bcrypt-nodejs';
import { Logger } from '../../../../models/logger';
import { User } from '../../../../models/user/User';
import { UserType } from '../../../../models/user/UserType';
import { validatePassword } from '../../../../helpers/helper_functions';
import { ValidationError } from 'objection';

const employees = express.Router();

const logger = Logger.Instance.getGrayLog();
const saltRounds = 10;

/**
 * @route       POST api/admin/employees/register
 * @description Create a user of type employee.
 * @access      Private
 */
employees.post('/register', async (req, res) => {

    const { firstName, lastName, email, username, password } = req.body;

    const pw = validatePassword(password);
    if (!pw['isValid']) return res.status(400).send({ error: pw['errors'] });

    bcrypt.genSalt(saltRounds, async (err, salt) => {

        bcrypt.hash(password, salt, undefined, async (err, hash) => {

            if (err) {
                console.log(err);
                logger.error('error with bcrypt', { errorData: err });
                return res.status(500).send({ error: 'Something went wrong with bcrypt.' });
            }

            try {

                await User
                    .query()
                    .insert({ firstName, lastName, email, username, password: hash, userType: UserType.employee });

                return res.status(200).send({ message: 'Successfuly registered an employee.' });

            }
            catch (error) {

                logger.error('employee registration failed', { error } );

                if (error instanceof ValidationError) {
                    const message: string = error.message;
                    return res.status(400).send({ message });
                }
                else {
                    return res.status(500).send({ error });
                }

            }

        });

    });

});

module.exports = employees;
