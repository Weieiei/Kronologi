import { hashPassword, validatePassword } from '../helpers/helper_functions';
import { Logger } from '../models/logger';
import { User } from '../models/user/User';
import { UserType } from '../models/user/UserType';
import { ValidationError } from 'objection';

const logger = Logger.Instance.getGrayLog();

export const createEmployee = async (req, res) => {

    const { firstName, lastName, email, username, password, services } = req.body.employee;

    try {
        validatePassword(password);
    }
    catch (error) {
        return res.status(400).send({ error: error.message });
    }

    try {

        const employee = await User.query()
            .insert({ firstName, lastName, email, username, password: await hashPassword(password), userType: UserType.employee });

        await employee.$relatedQuery('services')
            .relate(services);

        return res.status(200).send({ message: 'Successfully registered an employee.' });

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

};
