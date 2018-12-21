import { Model, JsonSchema, RelationMappings, ValidationError, snakeCaseMappers } from 'objection';
import { User } from '../user/User';
import { Service } from '../service/Service';
import moment from 'moment';
import { UserType } from '../user/UserType';

export class Appointment extends Model {

    readonly id!: number;
    userId?: number;
    employeeId?: number;
    serviceId?: number;
    startTime?: string;
    endTime?: string;
    notes?: string;
    createdAt?: Date;
    updatedAt?: Date;

    static get tableName(): string {
        return 'appointments';
    }

    static get columnNameMappers() {
        return snakeCaseMappers();
    }

    static get jsonSchema(): JsonSchema {
        return {
            type: 'object',
            required: ['userId', 'employeeId', 'serviceId', 'startTime'],
            properties: {
                id: {type: 'integer'},
                userId: {type: 'integer'},
                employeeId: {type: 'integer'},
                serviceId: {type: 'integer'},
                startTime: {type: 'string'},
                endTime: {type: 'string'},
                notes: {type: 'string'}
            }
        };
    }

    static get idColumn(): string {
        return 'id';
    }

    static get relationMappings(): RelationMappings {
        return {
            customer: {
                relation: Model.BelongsToOneRelation,
                modelClass: User,
                join: {
                    from: 'appointments.user_id',
                    to: 'users.id'
                }
            },
            employee: {
                relation: Model.HasOneRelation,
                modelClass: User,
                join: {
                    from: 'appointments.employee_id',
                    to: 'users.id'
                }
            },
            service: {
                relation: Model.HasOneRelation,
                modelClass: Service,
                join: {
                    from: 'appointments.service_id',
                    to: 'services.id'
                }
            }
        };
    }

    async $beforeInsert(queryContext) {
        /**
         * Get employee that we chose for the appointment.
         * If this 'employee' (user) isn't actually an employee, throw an error.
         * Get the services that this employee offers.
         * From those services, find the one with the serviceId assigned to the appointment model.
         * If we get nothing, it means either the employee doesn't offer it, or it just doesn't exist; throw an error.
         */
        const employee = await User
            .query()
            .where({id: this.employeeId})
            .first()
            .eager('[services]');

        if (employee.userType !== UserType.employee) {
            throw new ValidationError({
                message: 'You must choose a user of type employee.',
                type: 'InvalidUser'
            });
        }

        const services = employee['services'];
        const service = services.find(service => service.id === this.serviceId);

        if (!service) {
            throw new ValidationError({
                message: 'You must provide a valid service.',
                type: 'InvalidService'
            });
        }

        this.endTime = moment(this.startTime).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss');
        this.startTime += '-05';
        this.endTime += '-05';

        const appointments = await Appointment
            .query().select();
        if (appointments.length > 0) {
            const startTime = new Date(this.startTime);
            const endTime = new Date(this.endTime);
            for (let i = 0; i < appointments.length; i++) {
                const appointment_start_time = new Date(appointments[i].startTime);
                const appointment_end_time = new Date(appointments[i].endTime);
                // case 1: there exists an appointment that starts after this appointment and is still ongoing when this appointment ends
                // (the end time of this appointment is within an already an existing appointment)
                if (appointment_start_time <= endTime && appointment_end_time >= endTime) {
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
                // case 2: there exists an appointment that starts before this appointment and ends after the start of this appointment
                // (the start time of this appointment is within an existing appointment)
                else if (appointment_start_time <= startTime && appointment_end_time >= startTime) {
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
                // case 3: there exists an appointment that starts after this appointment and ends before the end of this appointment
                // (this appointment "surrounds" an existing appointment)
                else if (appointment_start_time >= startTime && appointment_end_time <= endTime) {
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
            }
            this.createdAt = new Date();
        }
    }
    async $beforeUpdate() {
        this.updatedAt = new Date();
    }
}



