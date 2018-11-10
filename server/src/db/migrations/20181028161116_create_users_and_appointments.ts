var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : new P(function (resolve) { resolve(result.value); }).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
exports.up = (knex) => __awaiter(this, void 0, void 0, function* () {
    yield knex.schema.raw('CREATE EXTENSION IF NOT EXISTS CITEXT;');
    yield knex.schema
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
});
exports.down = function (knex, Promise) {
    return knex.schema
        .dropTable('appointments')
        .dropTable('services')
        .dropTable('users');
};
//# sourceMappingURL=20181028161116_create_users_and_appointments.js.map