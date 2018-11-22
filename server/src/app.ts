import  express from "express";
import * as bodyParser from "body-parser";
import { Logger } from './models/logger';
const cors = require('cors');
const api = require('./api/api');

const logger = Logger.Instance.getGrayLog();

logger.on('error', function (error) {
    console.error('Error while trying to write to graylog2:', error);
});

logger.log("What we've got here is...failure to communicate", "Some men you just can't reach. So you get what we had here last week, which is the way he wants it... well, he gets it. I don't like it any more than you men.");

logger.log("What we've got here is...failure to communicate");

logger.log("What we've got here is...failure to communicate", { cool: 'beans' });

logger.log("What we've got here is...failure to communicate", "Some men you just can't reach. So you get what we had here last week, which is the way he wants it... well, he gets it. I don't like it any more than you men.", { cool: "beans" });

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
