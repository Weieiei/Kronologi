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

    async $beforeInsert(queryContext) {
        /**
         * TODO
         * appointment validation
         */
        // appointment validation (making sure there are no conflicts):
        // await super.$beforeInsert(queryContext);
        // const promise = this.validateNoConflict();
        const appointments = await Appointment
                .query().select();
        // .query().where({startTime: this.startTime}).orWhere({endTime: this.endTime})
        // .first();
        if (appointments.length > 0) {
            const startTime = new Date(this.startTime);
            const endTime = new Date(this.endTime);
            for (let i = 0; i < appointments.length; i++) {
                const appointment_start_time =  new Date(appointments[i].startTime);
                const appointment_end_time =  new Date(appointments[i].endTime);
                // case 1: there exists an appointment that starts after this appointment and is still ongoing when this appointment ends
                // (the end time of this appointment is within an already an existing appointment)
                if (appointment_start_time <= endTime && appointment_end_time >= endTime) {
                    console.log('Conflict: (case 1) an appointment is starting before your appointment ends.');
                    console.log ('Old appointment starts at: ' + appointment_start_time + ' and ends at: ' + appointment_end_time);
                    console.log ('New appointment starts at: ' + startTime + ' and ends at: ' + endTime);
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
                // case 2: there exists an appointment that starts before this appointment and ends after the start of this appointment
                // (the start time of this appointment is within an existing appointment)
                else if (appointment_start_time <= startTime && appointment_end_time >= startTime) {
                    console.log('Conflict: (case 2) an appointment is ongoing at the start of your appointment');
                    console.log ('Old appointment starts at: ' + appointment_start_time + ' and ends at: ' + appointment_end_time);
                    console.log ('New appointment starts at: ' + startTime + ' and ends at: ' + endTime);
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
                // case 3: there exists an appointment that starts after this appointment and ends before the end of this appointment
                // (this appointment "surrounds" an existing appointment)
                else if (appointment_start_time >= startTime && appointment_end_time <= endTime) {
                    console.log('Conflict: (case 3) an appointment is ongoing during your appointment');
                    console.log ('Old appointment starts at: ' + appointment_start_time + ' and ends at: ' + appointment_end_time);
                    console.log ('New appointment starts at: ' + startTime + ' and ends at: ' + endTime);
                    throw new ValidationError({
                        message: 'There is an already existing appointment creating a conflict',
                        type: 'appointment'
                    });
                }
            }

        }
        this.createdAt = new Date();
    }

    async $beforeUpdate() {
        this.updatedAt = new Date();
    }



}
