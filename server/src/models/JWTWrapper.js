const secretKey = 'secretKey';
const jwt = require('jsonwebtoken');
const error = 'Unauthorized request.';

class JWTWrapper {

    static generateToken(payload) {
        return jwt.sign(payload, secretKey);
    }

    static verifyToken(req, res, next) {
        if (!req.headers.authorization) {
            return res.status(401).send({ error });
        }
        
        let token = req.headers.authorization.split(' ')[1];
        if (token === 'null') {
            return res.status(401).send({ error });
        }

        let payload = jwt.verify(token, secretKey);
        console.log(payload);
        if (!payload) {
            return res.status(401).send({ error });            
        }

        req.userId = payload.subject;
        next();
    }

    static sign(payload) {
        return jwt.sign(payload, secretKey);
    }

}

module.exports = JWTWrapper;
