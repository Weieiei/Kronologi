import { Model, JsonSchema, RelationMappings, ValidationError, snakeCaseMappers } from 'objection';
import { User } from '../user/User';
import { Service } from '../service/Service';
import moment from 'moment';
import { UserType } from '../user/UserType';
import { AppointmentStatus } from './AppointmentStatus';

export class Appointment extends Model {

    readonly id!: number;
    clientId?: number;
    employeeId?: number;
    serviceId?: number;
    startTime?: Date;
    endTime?: Date;
    notes?: string;
    status?: string;
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

        this.checkIfClientAndEmployeeAreTheSame();
        const employee = await this.getEmployee();
        const service = this.getService(employee);

        this.endTime = new Date(moment(this.startTime).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss'));

        this.checkIfAppointmentFitsInShift(employee);
        this.checkIfAppointmentConflictsWithAnother(employee);

        this.status = AppointmentStatus.confirmed;
        this.createdAt = new Date();

    }

    async $beforeUpdate(options) {

        if (this.status !== AppointmentStatus.cancelled) {

            this.checkIfClientAndEmployeeAreTheSame();
            const employee = await this.getEmployee();
            const service = this.getService(employee);

            this.endTime = new Date(moment(this.startTime).add(service.duration, 'm').format('YYYY-MM-DD HH:mm:ss'));

            this.checkIfAppointmentFitsInShift(employee);
            this.checkIfAppointmentConflictsWithAnother(employee);

        } else {

            const oldAppointmentValues: Appointment = options.old;

            if (oldAppointmentValues.startTime < new Date(moment(new Date()).add(24, 'hours').format())) {

                throw new ValidationError({
                    message: 'Appointments can only be cancelled at least 24 hours in advance.',
                    type: 'CancellationError'
                });

            }

        }

        this.updatedAt = new Date();

    }

    checkIfClientAndEmployeeAreTheSame() {

        if (this.clientId === this.employeeId) {
            throw new ValidationError({
                message: 'You cannot book an appointment with yourself.',
                type: 'ClientIsEmployeeError'
            });
        }

    }

    async getEmployee() {

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

        return employee;

    }

    getService(employee: User) {

        const service = employee.services.find(service => service.id === this.serviceId);

        if (!service) {
            throw new ValidationError({
                message: `${employee.fullName} doesn't provided the desired service.`,
                type: 'InvalidService'
            });
        }

        return service;

    }

    checkIfAppointmentFitsInShift(employee: User) {

        const shiftIndex = employee.shifts.findIndex(shift => shift.startTime <= this.startTime && shift.endTime >= this.endTime);

        if (shiftIndex === -1) {
            throw new ValidationError({
                message: `This appointment does not fit into ${employee.fullName}'s schedule.`,
                type: 'EmployeeNotAvailable'
            });
        }

    }

    checkIfAppointmentConflictsWithAnother(employee: User) {

        const appointmentIndex = employee.employeeAppointments.findIndex(appointment => {
            return !((appointment.startTime >= this.startTime && appointment.startTime >= this.endTime) ||
                (appointment.endTime <= this.startTime && appointment.endTime <= this.endTime))
                && appointment.status !== AppointmentStatus.cancelled;
        });

        if (appointmentIndex !== -1) {
            throw new ValidationError({
                message: `This appointment conflicts with another in ${employee.fullName}'s schedule.`,
                type: 'AppointmentConflictError'
            });
        }

    }

}
