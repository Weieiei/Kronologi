import { Model, JsonSchema, RelationMappings, ValidationError, snakeCaseMappers } from 'objection';
import { User } from '../user/User';
import { Service } from '../service/Service';
import moment from 'moment';
import { UserType } from '../user/UserType';

export class Appointment extends Model {

    readonly id!: number;
    clientId?: number;
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
            required: ['clientId', 'employeeId', 'serviceId', 'startTime'],
            properties: {
                id: { type: 'integer' },
                clientId: { type: 'integer' },
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
                    from: 'appointments.client_id',
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
            .eager('[employeeAppointments, services, shifts]');

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

        // Check if appointment conflicts with another in the employee's schedule.
        const appointmentIndex = employee.employeeAppointments.findIndex(appointment => {
            return !((appointment.startTime >= this.startTime && appointment.startTime >= this.endTime) ||
                (appointment.endTime <= this.startTime && appointment.endTime <= this.endTime));
        });

        if (appointmentIndex !== -1) {
            throw new ValidationError({
                message: `This appointment conflicts with another in ${employee.fullName}'s schedule.`,
                type: 'AppointmentConflictError'
            });
        }

        this.createdAt = new Date();

    }
    async $beforeUpdate() {
        this.updatedAt = new Date();
    }
}
