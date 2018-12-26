exports.up = async knex => {

    await knex.schema
        .createTable('employee_service', table => {
            table.increments('id');
            table.integer('employee_id').unsigned().references('id').inTable('users');
            table.integer('service_id').unsigned().references('id').inTable('services');
            table.timestamp('created_at').defaultTo(knex.fn.now());
            table.timestamp('updated_at');
        })
        .createTable('employee_shifts', table => {
            table.increments('id');
            table.integer('employee_id').unsigned().references('id').inTable('users');
            table.timestamp('start_time');
            table.timestamp('end_time');
            table.timestamp('created_at').defaultTo(knex.fn.now());
            table.timestamp('updated_at');
        });

};

exports.down = knex => {
    return knex.schema
        .dropTableIfExists('employee_service')
        .dropTableIfExists('employee_shifts');
};
