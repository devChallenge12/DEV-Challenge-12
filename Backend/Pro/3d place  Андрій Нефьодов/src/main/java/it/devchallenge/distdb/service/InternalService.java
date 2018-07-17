package it.devchallenge.distdb.service;

import it.devchallenge.distdb.api.InternalUpdateRequest;
import it.devchallenge.distdb.api.UpdateRequest;
import it.devchallenge.distdb.dao.Dao;
import it.devchallenge.distdb.domain.Detail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InternalService {
    private final Dao dao;

    public void update(InternalUpdateRequest updateRequest) {
        final Detail latestData = dao.getLatestData(updateRequest.getKey());
        if (latestData!=null) {
            final LocalDateTime localLastUpdateTime = latestData.getUpdated();
            if (localLastUpdateTime.isAfter(updateRequest.getUpdated())) {
                log.warn("rejecting update. updateRequest: {}, localLastUpdateTime:{}", updateRequest,
                        localLastUpdateTime);
            }
        }
        dao.update(updateRequest.getKey(), updateRequest.getValue());
    }

    public void delete(String key) {
        dao.delete(key);
    }

}
