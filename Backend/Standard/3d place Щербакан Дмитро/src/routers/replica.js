const Router = require('koa-router');
const store = require('../store');

const router = new Router({
  prefix: '/replica'
});

const setReplica = async (ctx) => {
  const { key, value, timestamp } = ctx.request.body;
  const result = store.setReplicated(key, value, timestamp);

  ctx.status = result ? 200 : 422;
  ctx.body = {
    status: result ? 'ok' : 'error'
  };
};

const deleteReplica = async (ctx) => {
  const { key } = ctx.params;
  const result = store.delete(key);

  ctx.status = result ? 200 : 422;
  ctx.body = {
    status: result ? 'ok' : 'error'
  };
};

router.post('/', setReplica);
router.put('/', setReplica);
router.delete('/:key', deleteReplica);

module.exports = router;
