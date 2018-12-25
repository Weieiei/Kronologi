import { Model, JsonSchema, RelationMappings, snakeCaseMappers, ValidationError } from 'objection';
import { User } from '../user/User';

export class EmployeeShift extends Model {

    readonly id!: number;
    employeeId?: number;
    startTime?: Date;
    endTime?: Date;
    createdAt?: Date;
    updatedAt?: Date;

    static get tableName(): string {
        return 'employee_shifts';
    }

    static get columnNameMappers() {
        return snakeCaseMappers();
    }

    static get jsonSchema(): JsonSchema {
        return {
            type: 'object',
            required: ['employeeId', 'startTime', 'endTime'],
            properties: {
                id: { type: 'integer' },
                employeeId: { type: 'integer' },
                startTime: { type: 'date' },
                endTime: { type: 'date' }
            }
        };
    }

    static get idColumn(): string {
        return 'id';
    }

    static get relationMappings(): RelationMappings {
        return {
            employee: {
                relation: Model.BelongsToOneRelation,
                modelClass: User,
                join: {
                    from: 'employee_shifts.employee_id',
                    to: 'users.id'
                }
            }
        };
    }

    async $beforeInsert() {

        if (this.endTime <= this.startTime) {
            throw new ValidationError({
                message: 'A shift\'s start time should be before its end time.',
                type: 'InvalidShift'
            });
        }

        this.createdAt = new Date();

    }

    async $beforeUpdate() {
        this.updatedAt = new Date();
    }

}
