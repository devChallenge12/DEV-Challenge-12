data stored in memory only as suggested (which will be needed later)

each app instance has two networks:

- public - to serve client requests via rest api
- private - for node communication

communication inside cluster is done via broadcast udp, so no need to pass list of ip addresses as a configuration parameter, e.g. cluster is self discovering (something similar to elasticsearch) 

each request to front is replicated via udp

in case of maintanence or new node - it will have no data, because of in memory storage - so will take whole dataset from any machine which will respond to HELLO message, data stored in a such way that records wont be appended if they already here (starege :: save)



there is two use cases for this:

if eventual consistency is ok - round robin load balancer should be used, otherwise - sticky sessions in load balancer should be used

app made as docker container, so might be run in kubernetes which have both out of the box



in case of node loose connection to other nodes (e.g. no PING/PONG messages) node just dies, idea here is that it will be recreated by orchestrator and there is a cchance it will work on different machine with different network, so it will grab fresh data and start working




tests for stanalone mode `npm test`

local testing of cluster: `docker-compose up`

```bash
curl -X POST -H 'Content-Type: text/plain' http://localhost:3000/foo -d 'bar'
curl -X POST -H 'Content-Type: text/plain' http://localhost:3000/acme -d '42'
curl -X DELETE http://localhost:3000/foo
curl -X DELETE http://localhost:3000/acme
```

log for successfull demo

```bash
api2    | { type: 'STARTED', host: '10.1.0.12', port: '3000' }
api2    | {"type":"HELLO","from":"10.2.0.11"}
api1    | { type: 'STARTED', host: '0.0.0.0', port: '3000' }
api1    | {"type":"SYNC","data":{},"from":"10.2.0.12"}
api1    | {"type":"HTTP","method":"POST","key":"foo","val":"bar"}
api2    | {"type":"SAVE","key":"foo","val":"bar","by":"user1","ts":1529764730153,"from":"10.2.0.11"}
api1    | {"type":"HTTP","method":"POST","key":"acme","val":"42"}
api2    | {"type":"SAVE","key":"acme","val":"42","by":"user1","ts":1529764730183,"from":"10.2.0.11"}
api1    | {"type":"HTTP","method":"DELETE","key":"foo"}
api2    | {"type":"DELETE","key":"foo","from":"10.2.0.11"}
api1    | {"type":"HTTP","method":"DELETE","key":"acme"}
api2    | {"type":"DELETE","key":"acme","from":"10.2.0.11"}
```




