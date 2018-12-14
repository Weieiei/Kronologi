import { Model, JsonSchema, RelationMappings, snakeCaseMappers } from 'objection';
import { User } from '../user/User';
import {Service} from "../service/Service";

export class Appointment extends Model {

    readonly id!: number;
    userId?: number;
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
            required: ['userId', 'serviceId', 'startTime', 'endTime', 'notes'],
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

        this.createdAt = new Date();
    }

    async $beforeUpdate() {
        this.updatedAt = new Date();
    }

}
