const url = require('url');
const axios = require('axios');

const hosts = require('./config').allowedHosts;

const equalHosts = (host, matchedHost) => url.parse(host).host === url.parse(matchedHost).host;

const validHost = (host) => {
  try {
    url.parse(host);
    return true;
  } catch (e) {
    console.warn(e);
    return false;
  }
};

module.exports = currentHost => ({
  eachHost(callback) {
    hosts.forEach((host) => {
      if (validHost(host) && !equalHosts(currentHost, host)) {
        callback(host);
      }
    });
  },
  replicate(key, value, timestamp) {
    this.eachHost((host) => {
      axios
        .post(`${host}/replica`, { key, value, timestamp })
        .catch(e => console.warn(e));
    });
  },
  delete(key) {
    this.eachHost((host) => {
      axios
        .delete(`${host}/replica/${key}`)
        .catch(e => console.warn(e));
    });
  }
});
