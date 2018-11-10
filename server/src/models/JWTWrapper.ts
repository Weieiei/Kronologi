const secretKey = 'secretKey';
const jwt = require('jsonwebtoken');
class JWTWrapper {
    static generateToken(payload) {
        return jwt.sign(payload, secretKey);
    }
}
module.exports = JWTWrapper;
//# sourceMappingURL=JWTWrapper.js.map