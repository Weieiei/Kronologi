//process.env.NODE_ENV = "test";
var chai = require('chai'), chaiHttp = require('chai-http');

chai.use(chaiHttp);

const expect = chai.expect;

// describe('Checking whether the response return status 200', function() {
//     it('Status OK', async function(done) {
//         const { res, err } = await chai.request(server).get('/hello');
//         expect(res.body.message).to.equal('hello world');
//     });
// });
describe('registration best case scenario', () => {

    it('should return status 200 on call', () => {
        return chai.request('http://localhost:3000/')
            .post('api/authenticate/login')
            .type('form')
            .send({
                "username" : "password123",
                "password" : "password123"
            })
            .then(res => {
                chai.expect(res.status).to.eql(200);
            })
    })
   //   this.connector = new Connection().knex();
    // this.connector().table('users').del().where('_username',"melissa");

})
