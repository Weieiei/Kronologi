import chai from 'chai';
import chaiHttp from 'chai-http';
import { db } from '../src/db/knex';
import { Model } from 'objection';
import { Service } from '../src/models/service/Service';

Model.knex(db);
chai.use(chaiHttp);

describe('the functionality of GET api/services', () => {

    it('should return status 200 on call and match the data retrieved directly from the test database', () => {

        return chai.request('http://localhost:3000/').get('api/services').then(async res => {

            const services = await Service.query();

            services.forEach((service, i) => {
                chai.expect(service.id).to.equal(res.body[i].id);
                chai.expect(service.name).to.equal(res.body[i].name);
                chai.expect(service.duration).to.equal(res.body[i].duration);
            });

            chai.expect(res.status).to.eql(200);

        });

    });

});
