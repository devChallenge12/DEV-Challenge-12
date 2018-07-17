const keys = {};

module.exports = {
  get(key) {
    const detailed = this.getDetailed(key);

    if (detailed) {
      return detailed.value;
    }

    return null;
  },
  getDetailed(key) {
    return this.getValuesByKey(key, values => values[values.length - 1]);
  },
  getWithHistory(key) {
    return this.getValuesByKey(key, values => values);
  },
  getValuesByKey(key, callback) {
    return keys[key] ? callback(keys[key]) : null;
  },
  getAll() {
    return Object.entries(keys).reduce((dump, [key, values]) => ({
      ...dump,
      [key]: values[values.length - 1].value
    }), {});
  },
  getAllWithHistory() {
    return keys;
  },
  set(key, value, timestamp = Date.now()) {
    if (!keys[key]) {
      keys[key] = [];
    }

    keys[key].push({ value, timestamp });

    return timestamp;
  },
  setReplicated(key, value, timestamp) {
    const detailed = this.getDetailed(key);

    if (!detailed || detailed.timestamp < timestamp) {
      return this.set(key, value, timestamp);
    }

    return false;
  },
  delete(key) {
    if (keys[key]) {
      delete keys[key];
      return true;
    }

    return false;
  }
};
