import { Model, JsonSchema, RelationMappings, snakeCaseMappers, ValidationError } from 'objection';
import { Appointment } from '../appointment/Appointment';

export class User extends Model {

    readonly id!: number;
    firstName?: string;
    lastName?: string;
    email?: string;
    username?: string;
    password?: string;
    userType?: string;
    createdAt?: Date;
    updatedAt?: Date;

    appointments: Appointment[];

    static get tableName(): string {
        return 'users';
    }

    static get columnNameMappers() {
        return snakeCaseMappers();
    }

    /**
     * This is just a JSON schema, not the database schema.
     * It's only used for validation, i.e. whenever a model instance is created.
     */
    static get jsonSchema(): JsonSchema {
        return {
            type: 'object',
            required: ['firstName', 'lastName', 'email', 'username', 'password', 'userType'],
            properties: {
                id: { type: 'integer' },
                firstName: { type: 'string' },
                lastName: { type: 'string' },
                email: { type: 'string' },
                username: { type: 'string' },
                password: { type: 'string' },
                userType: { type: 'string' }
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
                    from: 'users.id',
                    to: 'appointments.user_id'
                }
            }
        };
    }

    get fullName() {
        return `${this.firstName} ${this.lastName}`;
    }

    async $beforeInsert() {
        this.validateUserData();
        await this.checkIfUserExists();
        this.createdAt = new Date();
    }

    async $beforeUpdate() {
        this.validateUserData();
        await this.checkIfUserExists();
        this.updatedAt = new Date();
    }

    validateUserData() {

        if (!this.firstName.length) {
            throw new ValidationError({
                message: 'First name should be at least 1 character long.',
                type: 'FirstNameError'
            });
        }
        else if (!this.lastName.length) {
            throw new ValidationError({
                message: 'Last name should be at least 1 character long.',
                type: 'LastNameError'
            });
        }
        else if (this.username.length < 4 || this.username.length > 30) {
            throw new ValidationError({
                message: 'Username should be between 4 and 30 characters.',
                type: 'UsernameError'
            });
        }
        else if (!/^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.][A-Za-z]{2,3}$/.test(this.email)) {
            throw new ValidationError({
                message: 'Please make sure to provide a valid email.',
                type: 'EmailError'
            });
        }

    }

    async checkIfUserExists() {

        const user = await User
            .query().where({ username: this.username }).orWhere({ email: this.email })
            .first();

        if (user) {
            if (user.username === this.username) {
                throw new ValidationError({
                    message: 'This username is taken.',
                    type: 'UniqueUsernameError'
                });
            }
            else if (user.email === this.email) {
                throw new ValidationError({
                    message: 'An account with this email already exists.',
                    type: 'UniqueEmailError'
                });
            }
        }

    }

}
