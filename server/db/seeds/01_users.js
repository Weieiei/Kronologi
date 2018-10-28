
exports.seed = function(knex, Promise) {
  // Deletes ALL existing entries
  return knex('users').del()
    .then(function () {
      // Inserts seed entries
      return knex('users').insert([
        { user_id: 1, first_name: 'Vartan', last_name: 'Benohanian', email: 'vartanbeno@gmail.com', username: 'vartanbeno', password: 'hello123' },
        { user_id: 2, first_name: 'John', last_name: 'Doe', email: 'johndoe@gmail.com', username: 'johndoe', password: 'test123' }
      ]);
    });
};
