package it.devchallenge.distdb.dao;

import it.devchallenge.distdb.domain.Server;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public class ServerDao {
    private final Map<UUID, String> data = new HashMap<>();

    public void update(UUID key, String value) {
        data.put(key, value);
    }

    public String getLatestValue(String key) {
        return data.get(key);
    }

    public void delete(String key) {
        data.remove(key);
    }

    public List<Server> getServers() {
        return data.entrySet().stream().map(e->new Server(e.getKey(), e.getValue())).collect(Collectors.toList());
    }

    public Map<UUID, String> getServersAsMap() {
        return data;
    }
}

