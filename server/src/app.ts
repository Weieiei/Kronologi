import  express from "express";
import * as bodyParser from "body-parser";
import { Logger } from './models/logger';
const cors = require('cors');
const api = require('./api/api');

const logger = Logger.Instance.getGrayLog();

const PORT = process.env.PORT || 3000;
const app = express();

declare global {
    namespace Express {
        interface Request {
            userId: number
        }
    }
}

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log('Running on port', PORT);
});
