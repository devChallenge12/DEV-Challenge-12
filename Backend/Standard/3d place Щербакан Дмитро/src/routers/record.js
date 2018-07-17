const Router = require('koa-router');
const store = require('../store');
const replication = require('../replication');

const router = new Router({
  prefix: '/record'
});

const setRecord = async (ctx) => {
  const { origin, body: { key, value } } = ctx.request;

  const timestamp = store.set(key, value);

  replication(origin).replicate(key, value, timestamp);

  ctx.body = {
    status: 'ok'
  };
};

const getRecord = async (ctx) => {
  const { key } = ctx.params;
  const withHistory = Object.keys(ctx.query).includes('history');

  ctx.body = {
    value: store[withHistory ? 'getWithHistory' : 'get'](key)
  };
};

const deleteRecord = (ctx) => {
  const { origin } = ctx.request;
  const { key } = ctx.params;
  const result = store.delete(key);
  if (result) {
    replication(origin).delete(key);
    ctx.status = 200;
    ctx.body = {
      status: 'ok',
    };
  } else {
    ctx.status = 404;
    ctx.body = {
      status: 'error',
      message: `There is no key ${key}`
    };
  }
};

router.get('/:key', getRecord);
router.delete('/:key', deleteRecord);
router.post('/', setRecord);
router.put('/', setRecord);

module.exports = router;
