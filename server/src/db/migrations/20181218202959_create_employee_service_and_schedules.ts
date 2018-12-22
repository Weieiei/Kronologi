exports.up = async knex => {

    await knex.schema
        .createTable('employee_service', table => {
            table.increments('id');
            table.integer('employee_id').unsigned().references('id').inTable('users');
            table.integer('service_id').unsigned().references('id').inTable('services');
        });

};

exports.down = knex => {
    return knex.schema
        .dropTableIfExists('employee_service');
};
