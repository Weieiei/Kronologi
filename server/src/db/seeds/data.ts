import bcrypt from "bcryptjs";
import { Admin } from '../../models/user/Admin';
import { Client } from '../../models/user/Client';

const saltRounds: any = 10;

async function hashPassword(password) {

	const hashedPassword = await new Promise((resolve, reject) => {

		bcrypt.hash(password, saltRounds, (err, hash) => {
			if (err) reject(err);
			resolve(hash);
		});

	})

	return hashedPassword;

}

exports.seed = (knex, Promise) => {

	return knex.select().from('services').then(services => {

		/**
		 * If there are no service entries in the services table,
		 * populate it with the appropriate rows.
		 * As well as the other tables.
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
				{ name: 'RECONNECT WITH YOUR BODY', duration: 210 }
			])
			.then(async () => {
				return knex('users').insert([
					{ first_name: 'John', last_name: 'Doe', email: 'johndoe@gmail.com', username: 'johndoe', password: await hashPassword('johndoe'), user_type: Client.name },
					{ first_name: 'Test', last_name: 'User', email: 'testuser@gmail.com', username: 'test', password: await hashPassword('test123'), user_type: Client.name },
					{ first_name: 'Admin', last_name: 'User', email: 'admin@gmail.com', username: 'admin', password: await hashPassword('admin123'), user_type: Admin.name }
				]);
			})
			.then(() => {
				return knex('appointments').insert([
					{ user_id: 2, service_id: 7, start_time: '2018-11-30 18:00:00-05', end_time: '2018-11-30 20:20:00-05', notes: 'Hello world' },
					{ user_id: 1, service_id: 12, start_time: '2018-12-02 19:30:00-05', end_time: '2018-12-02 22:30:00-05', notes: 'Some note' }
				])
			})

		}
	})
};
