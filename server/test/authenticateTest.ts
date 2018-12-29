process.env.NODE_ENV = "test";
import * as dotenv from "dotenv";
dotenv.config({ path: '../.env' });
import 'mocha';
const chai = require('chai'), chaiHttp = require('chai-http');
const express = require('express');
import {db} from "../src/db/knex";

chai.use(chaiHttp);
dotenv.config({ path: '../.env' });
describe('this set of tests will verify the functionality of POST api/authenticate/login', () => {
    it('login should return status 200 with correct credentials', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({"user":{"username": "test", "password": "test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(200);
            });
    });


    it('login should return status 401 if username does not exist', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({"user":{"username": "doesnotexist", "password": "test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == ("Incorrect username and/or password."));
            });
    });

    it('login should return status 401 if password for existing username is wrong', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .send({"user":{"username": "test", "password": "test123WRONG"}})
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == ("Incorrect username and/or password."));
            });
    });

});

describe('this set of tests will verify the functionality of POST api/authenticate/register', () => {

    var firstName="firstNameRegister";
    var lastName="lastNameRegister";
    var username="usernameRegister";
    //delete newly added entry
    debugger;
    // console.log(process.env.NODE_ENV)
     //console.log(db.client);

    (db.select().from('users').where("username", username)).del().then((data) => {console.log(data)
    });
    it('registration should return status 200 with correct input data', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({"user":{"firstName":firstName,"lastName":lastName,"email":"anaisCCCC@gmail.com","username":username,"password":"test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(200);
                console.log(JSON.parse(res.text));
            })
    });

    it('registration should return status 400 with username taken', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({"user":{"firstName":firstName,"lastName":lastName,"email":"anaisCoppernic@gmail.com","username":username,"password":"test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(400);
                chai.expect(JSON.parse(res.text).emailError == "This username is taken.");
            })
    });
});
