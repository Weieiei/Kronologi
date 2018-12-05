export {};
import 'mocha';
import {Connection} from "../src/db/knex";
const chai = require('chai'), chaiHttp = require('chai-http');
const knex = require('../src/db/knex');
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
//    this.connector = new Connection().knex();
//this.connector().table('users').del().where('_username',"melissa");

});

describe('registration with ', () => {

    it('should return status 200 on call', () => {
        return chai.request('localhost:3000/')
            .post('api/authenticate/register')
            .send({"user":{"_firstName":"lova","_lastName":"duong","_email":"gee@123.com","_username":"gee","_password":"test123"}})
            .then(res => {
                chai.expect(res.status).to.eql(200);
            })
    })
    //  this.connector = new Connection().knex();
    // this.connector().table('users').del().where('_username',"melissa");

})