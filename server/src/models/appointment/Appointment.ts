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
    startTime?: Date;
    endTime?: Date;
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
                startTime: { type: 'date' },
                endTime: { type: 'date' },
                notes: { type: 'string' }
            }
        };
    }

    static get idColumn(): string {
        return 'id';
    }

    static get relationMappings(): RelationMappings {
        return {
            client: {
                relation: Model.BelongsToOneRelation,
                modelClass: User,
                join: {
                    from: 'appointments.user_id',
                    to: 'users.id'
                }
            },
            employee: {
                relation: Model.BelongsToOneRelation,
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

    async $beforeInsert() {

        // Check if id of employee provided points to a user of type employee.
        const employee = await User.query()
            .where({ id: this.employeeId })
            .first()
            .eager('[appointments, services, shifts]');

        if (!employee || employee.userType !== UserType.employee) {
            throw new ValidationError({
                message: 'You must choose a user of type employee.',
                type: 'InvalidUser'
            });
        }

        // Check if id of service provided exists in the employee's offered services.
        const service = employee.services.find(service => service.id === this.serviceId);
        if (!service) {
            throw new ValidationError({
                message: `${employee.fullName} doesn't provided the desired service.`,
                type: 'InvalidService'
            });
        }

        this.endTime = new Date(moment(this.startTime).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss'));

        // Check if appointment's start and end times fit into one of the employee's shifts.
        const shiftIndex = employee.shifts.findIndex(shift => shift.startTime <= this.startTime && shift.endTime >= this.endTime);
        if (shiftIndex === -1) {
            throw new ValidationError({
                message: `This appointment does not fit into ${employee.fullName}'s schedule.`,
                type: 'EmployeeNotAvailable'
            });
        }

        /**
         * TODO
         * appointment validation
         * if we've gotten this far, it means that the appointment fits into the employee's shift
         * but does it conflict with other appointments?
         */

        this.createdAt = new Date();
    }
    async $beforeUpdate() {
        this.updatedAt = new Date();
    }
}
