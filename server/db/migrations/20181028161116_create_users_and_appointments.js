
exports.up = async knex => {
	await knex.schema.raw('CREATE EXTENSION IF NOT EXISTS CITEXT;');

	await knex.schema
	.createTable('users', table => {
		table.increments('user_id');
		table.string('first_name').notNullable();
		table.string('last_name').notNullable();
		table.specificType('email', 'CITEXT').unique().notNullable();
		table.specificType('username', 'CITEXT').unique().notNullable();
		table.string('password').notNullable();
		table.timestamp('created_at').defaultTo(knex.fn.now());
	})
	.createTable('appointments', table => {
		table.increments('appointment_id');
		table.integer('user_id').unsigned().references('user_id').inTable('users');
		table.timestamp('start_time').notNullable();
		table.timestamp('end_time').notNullable();
		table.string('notes');
	})
	.createTable('services', table => {
		table.increments('service_id');
		table.string('name').notNullable();
		table.integer('service_duration').unsigned();
	})
	await knex.schema.raw(`
		ALTER TABLE users
			ADD CONSTRAINT users_first_name_length CHECK (char_length(first_name) >= 2),
			ADD CONSTRAINT users_last_name_length CHECK (char_length(last_name) >= 2),
			ADD CONSTRAINT users_username_length CHECK (char_length(username) >= 4 AND char_length(username) <= 30);
	`);
};

exports.down = function(knex, Promise) {
	return knex.schema
		.dropTable('appointments')
		.dropTable('services')
		.dropTable('users');
};
