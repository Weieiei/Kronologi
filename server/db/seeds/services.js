
exports.seed = function(knex, Promise) {
    // Deletes ALL existing entries
    return knex('services').del()
      .then(function () {
        // Inserts seed entries
        return knex('services').insert([
          { name: 'BACK TO PURE LIFE', service_duration: 150},
          { name: 'QUICK VISIT TO THE SPA', service_duration: 100},
          { name: 'BUSINESS ONLY', service_duration: 120},
          { name: 'MEC EXTRA', service_duration: 200},
          { name: 'GET BACK ON TRACK', service_duration: 170},
          { name: 'SLENDER QUEST', service_duration: 200},
          { name: 'SERENITY', service_duration: 140},
          { name: 'ULTIMATE ESCAPE', service_duration: 160},
          { name: 'DIVINE RELAXATION', service_duration: 150},
          { name: '1/2 DAY PASSPORT', service_duration: 165},
          { name: 'THE FULL DAY PASSPORT', service_duration: 325},
          { name: 'POETRY FOR TWO', service_duration: 180},
          { name: 'ULTIMATE COUPLES TREAT', service_duration: 320},
          { name: 'BODY AFTER BABY', service_duration: 120},
          { name: 'LOST YOUR SOUL', service_duration: 120},
          { name: 'RECONNECT WITH YOUR BODY', service_duration: 210},
        ]);
      });
  };
  