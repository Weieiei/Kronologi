# [Knex.js](https://knexjs.org/)

Knex.js is a SQL query builder that's used to take away the need for the developer to work with raw queries (for the most part...). Click [here](https://devhints.io/knex) for a useful cheatsheet.

## Getting Started

First, install the Knex CLI via `npm i -g knex`.

### Migrations

Migration files allow us to create/update tables easily. While in the `server/db` directory (i.e. the one with the `knexfile`), simple running `knex migrate:make migration_name` will create a new migration file.

Once you've finished writing the migration, running `knex migrate:latest` will update your database to match your latest migrations.

If you happen to make updates to your migrations, first run `knex migrate:rollback`, then rerun `knex migrate:latest`.

### Seed Files

Seed files are used to populate your database with an initial set of data. To create a seed file, run `knex seed:make seed_name`.

This will generate a file in which there is an insert method. Using the table in which you want to insert rows, add in however many objects you want. When they're added, running `knex seed:run` will execute all seed files.

## Queries

### `SELECT`

```
// With raw queries, you get returned a bunch of information, like command, rowCount, rows, fields, etc.
knex.raw('SELECT * FROM users;').then(users => { ... });

// But this way, it filters out the rows for you.
knex.select().from('users').then(users => { ... });
```

### `SELECT ... WHERE`

```
knex.raw('SELECT * FROM users WHERE user_id = 1;').then(user => { ... });

knex.select().from('users').where('user_id', 1).then(user => { ... });
```

### `INSERT`

```
const { first_name, last_name, email, username, password } = req.body.user;

knex.raw(`
    INSERT INTO users (first_name, last_name, email, username, password)
    VALUES [?, ?, ?, ?, ?];
`, [first_name, last_name, email, username, password]).then(() => {
    ...
})

knex('users').insert({
    first_name: first_name,
    last_name,                  // shorthand
    email,
    username,
    password
}).then(() => { ... });
```

### `UPDATE`

```
const { user_id } = req.params;
const { first_name } = req.body;

knex.raw('UPDATE users SET first_name = ? WHERE user_id = ?;', [first_name, user_id]).then(() => {
    ...
});

knex('users').where('user_id', user_id).update({
    first_name
}).then(() => { ... });
```

### `DELETE`

```
const { user_id } = req.params;

knex.raw('DELETE FROM users WHERE user_id = ?;', user_id).then(() => { ... });

knex('users').where('user_id', user_id).del().then(() => { ... });
```
