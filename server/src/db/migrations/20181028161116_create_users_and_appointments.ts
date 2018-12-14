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
            table.string('user_type').notNullable();
            table.timestamp('created_at').defaultTo(knex.fn.now());
            table.timestamp('updated_at');
        })
        .createTable('services', table => {
            table.increments('id');
            table.string('name').notNullable();
            table.integer('duration').unsigned();
            table.timestamp('created_at').defaultTo(knex.fn.now());
            table.timestamp('updated_at');
        })
        .createTable('appointments', table => {
            table.increments('id');
            table.integer('user_id').unsigned().references('id').inTable('users');
            table.integer('service_id').unsigned().references('id').inTable('services');
            table.timestamp('start_time').notNullable();
            table.timestamp('end_time').notNullable();
            table.string('notes');
            table.timestamp('created_at').defaultTo(knex.fn.now());
            table.timestamp('updated_at');
        });
};

exports.down = function (knex, Promise) {
    return knex.schema
        .dropTableIfExists('appointments')
        .dropTableIfExists('services')
        .dropTableIfExists('users');
};
