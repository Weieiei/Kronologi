const secretKey = 'secretKey';
const jwt = require('jsonwebtoken');

class JWTWrapper {
    static generateToken(payload) {
        return jwt.sign(payload, secretKey);
    }

    static verifyToken(token) {
        return jwt.verify(token, secretKey);
    }
}

module.exports = JWTWrapper;
