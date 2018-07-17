module.exports = () => ({ request: { method, url, body } }, next) => {
  console.warn(`${method} ${url}`);
  if (Object.keys(body).length) {
    console.warn(body);
  }

  next();
};
