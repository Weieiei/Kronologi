const express = require('express'), 
    bodyParser = require('body-parser'),
    cors = require('cors');

const PORT = process.env.PORT || 3000;
const app = express();

app.use(cors());
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));

app.get('/', (req, res) => {
    res.send('Hey world');
})

app.listen(PORT, () => {
    console.log('Running on port', PORT)
})
