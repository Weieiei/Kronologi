const { Client } = require('pg'),
    config = require('./config');

/**
 * If environment variables are provided, use those.
 * Else, use local connection.
 */
const client = new Client({
    database: process.env.DB_NAME || config.database,
    port: process.env.DB_PORT || config.port,
    host: process.env.DB_HOST || config.host,
    user: process.env.DB_USER || config.user,
    password: process.env.DB_PASSWORD || config.password
})

client.connect();

module.exports = {
    query: (text, params, callback) => {
        return client.query(text, params, callback);
    }
}
