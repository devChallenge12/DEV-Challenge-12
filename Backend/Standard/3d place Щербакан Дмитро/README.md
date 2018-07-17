# DEV Challenge XII 
### Backend. Standard. Finals

Before start, ensure that `node.js` and `npm` are installed
To run project you need to run the following commands.

```bash
npm i
```

```bash
npm run demo
```

This will run two nodes on ports 3000 and 3001

##### Create or update record
Both examples will create record if it not exists, and update if exists.

```bash
curl -X POST \
  http://localhost:3000/record \
  -H 'Content-Type: application/json' \
  -d '{ "key": "lil", "value": "lil" }'
```

```bash
curl -X PUT \
  http://localhost:3000/record \
  -H 'Content-Type: application/json' \
  -d '{ "key": "lil", "value": "lil" }'
```

##### Get record
```bash
curl -X GET \
  'http://localhost:3000/record/lil' \
  -H 'Content-Type: application/json'
```
Where `lil` is a record's key

You can add query param `history` to see history:
```bash
curl -X GET \
  'http://localhost:3000/record/lil?history=' \
  -H 'Content-Type: application/json'
```
Delete record
```bash
curl -X DELETE \
  http://localhost:3000/record/lil
```
Where `lil` is a record's key

#####Dump all data
```bash
curl -X GET \
  http://localhost:3001/dump \
  -H 'Content-Type: application/json'
```
You can add query param `history` to see history:
```bash
curl -X GET \
  'http://localhost:3000/dump?history=' \
  -H 'Content-Type: application/json'
```
