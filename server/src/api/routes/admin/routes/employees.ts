import express from 'express';
import { Logger } from '../../../../models/logger';
import { User } from '../../../../models/user/User';
import { UserType } from '../../../../models/user/UserType';
import { hashPassword, validatePassword } from '../../../../helpers/helper_functions';
import { ValidationError } from 'objection';

const employees = express.Router();

const logger = Logger.Instance.getGrayLog();

/**
 * @route       POST api/admin/employees/register
 * @description Create a user of type employee.
 * @access      Private
 */
employees.post('/register', async (req, res) => {

    const { firstName, lastName, email, username, password } = req.body;

    try {
        validatePassword(password);
    }
    catch (error) {
        return res.status(400).send({ error: error.message });
    }

    try {

        await User
            .query()
            .insert({ firstName, lastName, email, username, password: await hashPassword(password), userType: UserType.employee });

        return res.status(200).send({ message: 'Successfuly registered an employee.' });

    }
    catch (error) {

        logger.error('employee registration failed', { error } );

        if (error instanceof ValidationError) {
            return res.status(400).send({ error: error.message });
        }
        else {
            return res.status(500).send({ error });
        }

    }

});

module.exports = employees;
