const Router = require('koa-router');
const store = require('../store');

const router = new Router();

const dumpStore = async (ctx) => {
  const withHistory = Object.keys(ctx.query).includes('history');
  ctx.body = store[withHistory ? 'getAllWithHistory' : 'getAll']();
};

router.get('/dump', dumpStore);

module.exports = router;
