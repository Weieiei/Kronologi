import { Model, JsonSchema, RelationMappings, ValidationError, snakeCaseMappers } from 'objection';
import { User } from '../user/User';
import { Service } from '../service/Service';

export class Appointment extends Model {

    readonly id!: number;
    userId?: number;
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
            required: ['userId', 'serviceId', 'startTime', 'endTime'],
            properties: {
                id: { type: 'integer' },
                userId: { type: 'integer' },
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
         * TODO
         * appointment validation
         */
        // appointment validation (making sure there are no conflicts):
        this.validateNoConflict();
        this.createdAt = new Date();
    }

    async $beforeUpdate() {
        this.updatedAt = new Date();
    }

    async validateNoConflict() {

        const appointment = await Appointment
            .query().where({startTime: this.startTime}).orWhere({endTime: this.endTime})
            .first();

        // case 1: there exists an appointment that starts after this appointment and is still ongoing when this appointment ends
        // (the end time of this appointment is within an already an existing appointment)
        if (appointment) {
            if (appointment.startTime >= this.startTime && appointment.startTime <= this.endTime ) {
                console.log('Conflict: an appointment is starting before your appointment ends.');
                throw new ValidationError({
                    message: 'There is an already existing appointment creating a conflict',
                    type: 'appointment'
                });
            }

        }
    }

}
