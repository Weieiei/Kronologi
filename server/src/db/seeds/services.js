exports.seed = function(knex, Promise) {
  
  return knex.select().from('services').then(services => {
    
    /**
     * If there are no service entries in the services table,
     * populate it with the appropriate rows.
     */
    if (!services.length) {

      return knex('services').insert([
            { name: 'BACK TO PURE LIFE', duration: 150 },
            { name: 'QUICK VISIT TO THE SPA', duration: 100 },
            { name: 'BUSINESS ONLY', duration: 120 },
            { name: 'MEC EXTRA', duration: 200 },
            { name: 'GET BACK ON TRACK', duration: 170 },
            { name: 'SLENDER QUEST', duration: 200 },
            { name: 'SERENITY', duration: 140 },
            { name: 'ULTIMATE ESCAPE', duration: 160 },
            { name: 'DIVINE RELAXATION', duration: 150 },
            { name: '1/2 DAY PASSPORT', duration: 165 },
            { name: 'THE FULL DAY PASSPORT', duration: 325 },
            { name: 'POETRY FOR TWO', duration: 180 },
            { name: 'ULTIMATE COUPLES TREAT', duration: 320 },
            { name: 'BODY AFTER BABY', duration: 120 },
            { name: 'LOST YOUR SOUL', duration: 120 },
            { name: 'RECONNECT WITH YOUR BODY', duration: 210 },
      ]);

    }

  })

};
