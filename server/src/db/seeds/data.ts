import { UserType } from '../../models/user/UserType';
import { Service } from '../../models/service/Service';
import { db } from '../knex';
import { Model } from 'objection';
import { User } from '../../models/user/User';
import { Appointment } from '../../models/appointment/Appointment';
import { hashPassword } from '../../helpers/helper_functions';

Model.knex(db);

exports.seed = async () => {

    const allUsers = await User.query().count();
    const count = allUsers[0]['count'];

	if (!parseInt(count)) {

        const users = await User.query().insertGraph([
            { firstName: 'John', lastName: 'Doe', email: 'johndoe@johndoe.com', username: 'johndoe', password: await hashPassword('johndoe123'), userType: UserType.client },
            { firstName: 'Test', lastName: 'User', email: 'test@testuser.com', username: 'test', password: await hashPassword('test123'), userType: UserType.client },
            { firstName: 'Admin', lastName: 'User', email: 'admin@admin.com', username: 'admin', password: await hashPassword('admin123'), userType: UserType.admin },
            { firstName: 'Employee', lastName: 'User', email: 'employee@employee.com', username: 'employee', password: await hashPassword('employee123'), userType: UserType.employee }
        ]);

		const services = await Service.query().insertGraph([
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
        ]);

		const employee = users[3];
		await employee.$relatedQuery('services').relate([
		    services[0].id, services[1].id, services[3].id, services[6].id, services[8].id, services[11].id
        ]);

		await Appointment.query().insertGraph([
            { userId: users[1].id, employeeId: employee.id, serviceId: services[6].id, startTime: '2019-11-30 18:00:00', notes: 'Hello world' },
            { userId: users[0].id, employeeId: employee.id, serviceId: services[11].id, startTime: '2019-12-02 19:30:00', notes: 'Some note' }
        ]);

	}

};
