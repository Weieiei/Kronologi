// process.env.NODE_ENV = "test";
// import 'mocha';
// import {db} from "../src/db/knex";
//
// const chai = require('chai'), chaiHttp = require('chai-http');
// const express = require('express');
// chai.use(chaiHttp);
//
// describe('this set of tests will verify the functionality of GET api/services', () => {
//     let connector = db;
//     it('login should return status 401 if password for existing username is wrong', () => {
//         return chai.request('http://localhost:3000/')
//             .get('api/services')
//             .then(res => {
//                 let resArray=Array.of(res.body)[0];
//                 console.log("****");
//                 console.log(resArray[0]["id"]);
//                 (connector.select().from('services').then((data) => {
//                 let dbArray=Array.of(data)[0];
//                // console.log(Array.of(data));
//             //    console.log(dbArray[0]);
//                 }));
//                // chai.expect(res.status).to.eql(401);
//               //  chai.expect(JSON.parse(res.text).invalidCredentials == ("Incorrect username and/or password."));
//             });
//     });
//
// });
