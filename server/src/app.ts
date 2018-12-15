import express from 'express';
import * as bodyParser from 'body-parser';
import cors from 'cors';
import * as dotenv from 'dotenv';

dotenv.config();

const api = require('./api/api');
const app = express();

const PORT = process.env.PORT;

declare global {
    namespace Express {
        interface Request {
            userId: number;
        }
    }
}

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log(`Running ${process.env.NODE_ENV} environment on port ${PORT}`);
});
