import { Model, JsonSchema, RelationMappings, snakeCaseMappers, ValidationError } from 'objection';
import { User } from '../user/User';
import { Service } from '../service/Service';
import moment from 'moment';
import {UserType} from "../user/UserType";

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
                id: { type: 'integer' },
                userId: { type: 'integer' },
                employeeId: { type: 'integer' },
                serviceId: { type: 'integer' },
                startTime: { type: 'string' },
                endTime: { type: 'string' },
                notes: { type: 'string' }
            }
        };
    }

    static get idColumn(): string {
        return 'id';
    }

    static get relationMappings(): RelationMappings {
        return {
            user: {
                relation: Model.BelongsToOneRelation,
                modelClass: User,
                join: {
                    from: 'appointments.user_id',
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

    async $beforeInsert() {

        /**
         * Get employee that we chose for the appointment.
         * If this 'employee' (user) isn't actually an employee, throw an error.
         * Get the services that this employee offers.
         * From those services, find the one with the serviceId assigned to the appointment model.
         * If we get nothing, it means either the employee doesn't offer it, or it just doesn't exist; throw an error.
         */
        const employee = await User
            .query()
            .where({ id: this.employeeId })
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

        /**
         * TODO
         * appointment validation
         */

        this.createdAt = new Date();
    }

    async $beforeUpdate() {
        this.updatedAt = new Date();
    }

}
