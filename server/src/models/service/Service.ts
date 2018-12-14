import { Model, JsonSchema, snakeCaseMappers, RelationMappings } from 'objection';
import { Appointment } from '../appointment/Appointment';

export class Service extends Model {

    readonly id!: number;
    name?: string;
    duration?: number;
    createdAt?: Date;
    updatedAt?: Date;

    static get tableName(): string {
        return 'services';
    }

    static get columnNameMappers() {
        return snakeCaseMappers();
    }

    static get jsonSchema(): JsonSchema {
        return {
            type: 'object',
            required: ['name', 'duration'],
            properties: {
                id: { type: 'integer' },
                name: { type: 'string' },
                duration: { type: 'integer' }
            }
        };
    }

    static get idColumn(): string {
        return 'id';
    }

    static get relationMappings(): RelationMappings {
        return {
            appointments: {
                relation: Model.HasManyRelation,
                modelClass: Appointment,
                join: {
                    from: 'services.id',
                    to: 'appointments.service_id'
                }
            }
        };
    }

    $beforeInsert() {
        this.createdAt = new Date();
    }

    $beforeUpdate() {
        this.updatedAt = new Date();
    }

}
