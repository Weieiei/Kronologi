import * as express from "express"

const authenticate = require('./routes/authenticate');

let apiTemp: express.Router;
apiTemp = express.Router();

export let api = apiTemp.get('/', (req , res ) => {
    res.send({ message: 'Hey world' });
});
api.use('/authenticate', authenticate);

//# sourceMappingURL=api.js.map