package it.devchallenge.distdb.service;

import it.devchallenge.distdb.api.UpdateRequest;
import it.devchallenge.distdb.dao.Dao;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import java.time.LocalDateTime;
import java.util.Map;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UpdateService {
    private final Dao dao;
    private final ReplicationService replicationService;

    public void update(UpdateRequest updateRequest) {
        dao.update( updateRequest.getKey(), updateRequest.getValue());
        replicationService.update(updateRequest.getKey(), updateRequest.getValue(), LocalDateTime.now());
    }

    public String getLatestValue(String key) {
        return dao.getLatestValue(key);
    }

    public void delete(String key) {
        dao.delete(key);
        replicationService.delete(key);
    }

    public Map<String, String> dump() {
        return dao.dump();
    }
}
