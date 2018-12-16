import { JWTWrapper } from '../wrappers/JWTWrapper';
import { RequestWrapper } from '../wrappers/RequestWrapper';

const error = 'Unauthorized request.';

/**
 * Check if the JWT in the header is valid.
 * If it is, store the user ID and type in the request, and move on to the next function in the request-response cycle.
 * If it's not, return an error.
 */
export function userMiddleware(req: RequestWrapper, res, next) {

    if (!req.headers.authorization) {
        return res.status(401).send({ error });
    }

    const token = req.headers.authorization.split(' ')[1];
    if (token === 'null') {
        return res.status(401).send({ error });
    }

    const payload: object | string = JWTWrapper.verifyToken(token);
    if (!payload) {
        return res.status(401).send({ error });
    }

    req.userId = payload['subject'];
    req.userType = payload['type'];

    next();

}
