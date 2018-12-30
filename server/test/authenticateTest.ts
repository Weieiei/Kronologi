import * as dotenv from 'dotenv';
import chai from 'chai';
import chaiHttp from 'chai-http';
import { db } from '../src/db/knex';
import { User } from '../src/models/user/User';
import { Model } from 'objection';

Model.knex(db);

dotenv.config({ path: '../.env' });
process.env.NODE_ENV = 'test';

chai.use(chaiHttp);

describe('the functionality of POST api/authenticate/login', () => {

    it('login should return status 200 with correct credentials', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({ user: { username: 'test', password: 'test123' } })
            .then(res => {
                chai.expect(res.status).to.eql(200);
            });
    });


    it('login should return status 401 if username does not exist', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({ user: { username: 'doesnotexist', password: 'test123' } })
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == 'Incorrect username and/or password.');
            });
    });

    it('login should return status 401 if password for existing username is wrong', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({ user: { username: 'test', password: 'test123WRONG' } })
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == 'Incorrect username and/or password.');
            });
    });

});

describe('the functionality of POST api/authenticate/register', () => {

    const firstName = 'firstNameRegister';
    const lastName = 'lastNameRegister';
    const username = 'usernameRegister';

    User.query().delete().where({ username }).then(() => { });

    it('registration should return status 200 with correct input data', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({ user: { firstName, lastName, email: 'anaisCCCC@gmail.com', username, 'password': 'test123' } })
            .then(res => {
                chai.expect(res.status).to.eql(200);
                console.log(JSON.parse(res.text));
            });
    });

    it('registration should return status 400 with username taken', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({ user: { firstName, lastName, 'email': 'anaisCoppernic@gmail.com', username, 'password': 'test123' } })
            .then(res => {
                chai.expect(res.status).to.eql(400);
                chai.expect(JSON.parse(res.text).emailError == 'This username is taken.');
            });
    });

});
