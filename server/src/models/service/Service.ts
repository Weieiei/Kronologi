import { Model, JsonSchema, snakeCaseMappers, RelationMappings } from 'objection';
import { Appointment } from '../appointment/Appointment';
import { User } from '../user/User';

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
            },
            employees: {
                relation: Model.ManyToManyRelation,
                modelClass: User,
                join: {
                    from: 'services.id',
                    through: {
                        from: 'employee_service.service_id',
                        to: 'employee_service.employee_id'
                    },
                    to: 'users.id'
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
