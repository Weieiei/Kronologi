import  express from "express";
import * as bodyParser from "body-parser";
var api = require('./api/api');
var cors = require('cors');

var graylog2 = require("graylog2");
var logger = new graylog2.graylog({
    servers: [
      { 'host': '127.0.0.1', port: 12201 },
      { 'host': '0.0.0.0', port: 12201}
    ],
    hostname: 'server.name', // the name of this host
                             // (optional, default: os.hostname())
    facility: 'Node.js',     // the facility for these log messages
                             // (optional, default: "Node.js")
    bufferSize: 1350         // max UDP packet size, should never exceed the
                             // MTU of your system (optional, default: 1400)
});

logger.on('error', function (error) {
    console.error('Error while trying to write to graylog2:', error);
});
logger.log("What we've got here is...failure to communicate", "Some men you just can't reach. So you get what we had here last week, which is the way he wants it... well, he gets it. I don't like it any more than you men.");

logger.log("What we've got here is...failure to communicate");

logger.log("What we've got here is...failure to communicate", { cool: 'beans' });

logger.log("What we've got here is...failure to communicate", "Some men you just can't reach. So you get what we had here last week, which is the way he wants it... well, he gets it. I don't like it any more than you men.",
    {
        cool: "beans"
    }
);

const PORT = process.env.PORT || 3000;
const app = express();

  
app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.use('/api', api);

app.listen(PORT, () => {
    console.log('Running on port', PORT);
});