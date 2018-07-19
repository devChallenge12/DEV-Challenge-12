import redis


class DataBase:

    _encoding = "utf-8"

    def __init__(self, data_db, settings_db):

        self.data_db = data_db
        self.settings_db = settings_db

        self.data_engine = redis.from_url(data_db)
        self.settings_engine = redis.from_url(settings_db)

    def update(self, name, value, namespace="data"):
        value = value.encode(self._encoding)
        key ="{}:{}".format(namespace, name)
        return self.data_engine.set(key, value)


    def get(self, name, namespace="data"):
        key = "{}:{}".format(namespace, name)
        value = self.data_engine.get(key)
        if value:
            value = value.decode(self._encoding)
        return value


    def remove(self, name, namespace="data"):
        key = "{}:{}".format(namespace, name)
        return self.data_engine.delete(key)

    def dump(self, namespace="data"):
        pattern = "{}*".format(namespace)
        keys = self.data_engine.keys(pattern)
        if not keys:
            return None

        values = self.data_engine.mget(keys)
        if not values:
            return None

        #encoding
        keys = [key.decode(self._encoding) for key in keys]
        values = [value.decode(self._encoding) for value in values]

        #deletin namespace from keys
        names = [key.replace("{}:".format(namespace), "") for key in keys]

        data = dict(zip(names, values))

        return data


    def add_nodes(self, node, namespace="settings"):

        key = "{}:{}".format(namespace, "nodes")
        node = node.encode(self._encoding)

        return self.settings_engine.sadd(key, node)

    def get_nodes(self, namespace="settings"):

        key = "{}:{}".format(namespace, "nodes")

        nodes = self.settings_engine.smembers(key)

        # encoding
        nodes = [node.decode(self._encoding) for node in nodes]

        return nodes

    def remove_node(self, node, namespace="settings"):
        key = "{}:{}".format(namespace, "nodes")

        # encoding
        node = node.encode(self._encoding)

        return self.settings_engine.srem(key, node)



def init_db(config):

    data_db = config["redis_datastore"]
    settings_db = config["redis_settingstore"]

    return DataBase(data_db, settings_db)

