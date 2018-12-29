process.env.NODE_ENV = "test";
import 'mocha';
import {db} from "../src/db/knex";

const chai = require('chai'), chaiHttp = require('chai-http');
const express = require('express');
chai.use(chaiHttp);

describe('this set of tests will verify the functionality of GET api/services', () => {
    it('should return status 200 on call and match the data retrieved directly from the test database', () => {
        return chai.request('http://localhost:3000/')
            .get('api/services')
            .then(res => {
                let index = 0;
                (db.select().from('services').then((data) => {
                    for (let each of res.body) {
                        chai.expect(each.id).to.equal(data[index].id);
                        chai.expect(each.name).to.equal(data[index].name);
                        chai.expect(each.duration).to.equal(data[index].duration);
                        index++;
                    }
                }));
                chai.expect(res.status).to.eql(200);
            });
    });

});
