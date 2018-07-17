!# /usr/bin/env bash

curl -X POST -H 'Content-Type: text/plain' http://localhost:3000/foo -d 'bar'
curl -X POST -H 'Content-Type: text/plain' http://localhost:3000/acme -d '42'
curl -X DELETE http://localhost:3000/foo
curl -X DELETE http://localhost:3000/acme

