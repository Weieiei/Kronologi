import { UserType } from '../models/user/UserType';
import { RequestWrapper } from '../wrappers/RequestWrapper';

const error = 'Unauthorized request.';

/**
 * Check if the user is of type admin.
 * Should be called after the userMiddleware.
 * The user type is stored in the request.
 */
export function adminMiddleware(req: RequestWrapper, res, next) {

    if (req.userType !== UserType.admin) {
        return res.status(401).send({ error });
    }

    next();

}
