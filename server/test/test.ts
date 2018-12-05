//export {};
import {Service} from "../src/models/service/service";

process.env.NODE_ENV = "test";
import 'mocha';
import {Connection} from "../src/db/knex";
const chai = require('chai'), chaiHttp = require('chai-http');
const express = require('express');
chai.use(chaiHttp);

describe('login with correct credentials', () => {

    it('should return status 200 on call', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .type('form')
            .send({
                "username" : "test",
                "password" : "test123"
            })
            .then(res => {
                chai.expect(res.status).to.eql(200);
            });
    });

});

describe('registration with correct information', () => {

    it('should return status 200 on call', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({"user":{"_firstName":"billy","_lastName":"duong","_email":"billy@123.com","_username":"billy","_password":"test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(200);
            })
    })
    let connector = new Connection().knex();
    debugger
    //delete newly added entry
    (connector.select().from('users').where("username", "billy")).del().then((data) => {
        console.log(data);
    });

})
