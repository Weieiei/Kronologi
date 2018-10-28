
exports.up = async knex => {
	await knex.schema.createTable('users', (table) => {
		table.increments('user_id');
		table.string('first_name').notNullable();
		table.string('last_name').notNullable();
		table.string('email').unique().notNullable();
		table.string('username').unique().notNullable();
		table.string('password').notNullable();
		table.timestamp('created_at').defaultTo(knex.fn.now());
	})
	
	await knex.schema.raw(`
		ALTER TABLE users
			ADD CONSTRAINT users_first_name_length CHECK (char_length(first_name) >= 2),
			ADD CONSTRAINT users_last_name_length CHECK (char_length(last_name) >= 2),
			ADD CONSTRAINT users_username_length CHECK (char_length(username) >= 4 AND char_length(username) <= 30);
	`);
};

exports.down = function(knex, Promise) {
	return knex.schema.dropTable('users');
};
