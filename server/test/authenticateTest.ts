process.env.NODE_ENV = "test";
import 'mocha';
import {Connection} from "../src/db/knex";

const chai = require('chai'), chaiHttp = require('chai-http');
const express = require('express');
chai.use(chaiHttp);

describe('this set of tests will verify the functionality of POST api/authenticate/register', () => {

    it('login should return status 200 with correct credentials', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .type('form')
            .send({
                "username": "test",
                "password": "test123"
            })
            .then(res => {
                chai.expect(res.status).to.eql(200);
            });
    });

    it('login should return status 401 if username does not exist', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .type('form')
            .send({
                "username": "doesnotexisthehe",
                "password": "test123"
            })
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == ("Incorrect username and/or password."));
            });
    });

    it('login should return status 401 if password for existing username is wrong', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .type('form')
            .send({
                "username": "test123",
                "password": "wrongpasswordguesswhathehe"
            })
            .then(res => {
                chai.expect(res.status).to.eql(401);
                chai.expect(JSON.parse(res.text).invalidCredentials == ("Incorrect username and/or password."));
            });
    });

});

describe('this set of tests will verify the functionality of POST api/authenticate/register', () => {

    let connector = new Connection().knex();
    //delete newly added entry
    (connector.select().from('users').where("username", "bella")).del().then((data) => {
    });

    it('registration should return status 200 with correct input data', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({
                "user": {
                    "_firstName": "bella",
                    "_lastName": "duong",
                    "_email": "bella@123.com",
                    "_username": "bella",
                    "_password": "test123"
                }
            })
            .then(res => {
                chai.expect(res.status).to.eql(200);
                console.log(JSON.parse(res.text));
                (connector.select().from('users').where("username", "bella")).del().then((data) => {
                });;
            })
    });

 it('registration should return status 400 with username taken', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({
                "user": {
                    "_firstName": "111",
                    "_lastName": "1",
                    "_email": "random@123.com",
                    "_username": "johndoe",
                    "_password": "test123"
                }
            })
            .then(res => {
                chai.expect(res.status).to.eql(400);
                chai.expect(JSON.parse(res.text).emailError == "This username is taken.");
                (connector.select().from('users').where("username", "bella")).del().then((data) => {
                });
            });
    });
});
