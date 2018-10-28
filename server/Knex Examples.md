# Knex.js

Knex.js is a SQL query builder that's used to take away the need for the developer to work with raw queries (for the most part...).

Here are some examples of raw queries, and their Knex counterpart.

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
