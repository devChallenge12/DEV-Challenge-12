const Koa = require('koa');
const bodyParser = require('koa-bodyparser');

const record = require('./routers/record');
const replica = require('./routers/replica');
const dump = require('./routers/dump');

const logger = require('./logger');

const app = new Koa();

const port = process.env.PORT || 3000;

app.use(bodyParser());
app.use(logger());

app.use(record.routes());
app.use(replica.routes());
app.use(dump.routes());

module.exports = {
  run: () => {
    app.listen(port);
    console.log(`Listening on port ${port}`); // eslint-disable-line no-console
  }
};
