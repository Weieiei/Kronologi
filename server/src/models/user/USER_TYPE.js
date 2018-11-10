/**
 * Using this as an enum for types of users.
 * It's better to use a string rather than a binary true/false value.
 * What if we wanna add employees? We'd just add a 3rd possible type.
 * Also better to use string than integers. It costs a bit more than space,
 * but saves a lot of trouble in the long run.
 */
const USER_TYPE = {

    CLIENT: 'client',
    ADMIN: 'admin'

}

module.exports = USER_TYPE;
