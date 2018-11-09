exports.up = async knex => {
    await knex.schema.raw('CREATE EXTENSION IF NOT EXISTS CITEXT;');

    await knex.schema
        .createTable('users', table => {
            table.increments('id');
            table.string('first_name').notNullable();
            table.string('last_name').notNullable();
            table.specificType('email', 'CITEXT').unique().notNullable();
            table.specificType('username', 'CITEXT').unique().notNullable();
            table.string('password').notNullable();
            table.timestamp('created_at').defaultTo(knex.fn.now());
        })
        .createTable('services', table => {
            table.increments('id');
            table.string('name').notNullable();
            table.integer('duration').unsigned();
        })
        .createTable('appointments', table => {
            table.increments('id');
            table.integer('user_id').unsigned().references('id').inTable('users');
            table.integer('service_id').unsigned().references('id').inTable('services');
            table.timestamp('start_time').notNullable();
            table.timestamp('end_time').notNullable();
            table.string('notes');
        });
};

exports.down = function (knex, Promise) {
    return knex.schema
        .dropTable('appointments')
        .dropTable('services')
        .dropTable('users');
};
