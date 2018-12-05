//process.env.NODE_ENV = "test";
var chai = require('chai'), chaiHttp = require('chai-http');

chai.use(chaiHttp);

const expect = chai.expect;

describe('registration best case scenario', () => {

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
    //   this.connector = new Connection().knex();
    // this.connector().table('users').del().where('_username',"melissa");

});