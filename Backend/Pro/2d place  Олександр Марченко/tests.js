const assert = require('assert')
const request = require('supertest')
const axios = require('axios')
const app = require('./app')
describe('singlenode', () => {
    it('should be CRUDy', done => {
        // CREATE
        request(app)
            .post('/acme')
            .set('Content-Type', 'text/plain')
            .send('42')
            .then(res => {
                assert.equal(res.status, 200)

                // READ
                return request(app)
                    .get('/acme')
                    .then(res => {
                        assert.equal(res.status, 200)
                        assert.equal(res.get('content-type'), 'text/plain; charset=utf-8')
                        assert.ok(res.text)
                        assert.equal(res.text, '42')
                        
                        // DUMP
                        return request(app)
                            .get('/')
                            .then(res => {
                                assert.equal(res.status, 200)
                                assert.ok(res.text.indexOf('acme: 42') !== -1)

                                // DELETE
                                return request(app)
                                    .delete('/acme')
                                    .then(res => {
                                        assert.equal(res.status, 200)
                                        done()
                                    })

                                done()
                            })
                    })
            })
            .catch(done)
    })

    it('should get 404 for get', done => {
        request(app)
            .get('/INVALID_KEY')
            .then(res => {
                assert.equal(res.status, 404)
                done()
            })
            .catch(done)
    })

    it('should get 200 for delete event if no key', done => {
        request(app)
            .delete('/INVALID_KEY')
            .then(res => {
                assert.equal(res.status, 200)
                done()
            })
            .catch(done)
    })
})

describe('integration', () => {
    // const api1 = 'http://localhost:3000/'
    // const api2 = 'http://localhost:3001/'
    // it('should replicate', done => {
    //     axios.post(`${api1}/hello`, 'world', {headers: {'Content-Type': 'text/plain'}}).then(res => {
    //         assert.equal(res.status, 200)

    //         axios.get(`${api2}/hello`).then(res => {
    //             assert.equal(res.data, 'world')
    //         })

    //         done()
    //     }).catch(done)
    // })
})