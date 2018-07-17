package it.devchallenge.distdb.client;

import it.devchallenge.distdb.api.InternalUpdateRequest;
import it.devchallenge.distdb.api.UpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class ReplicationClient {
    private final Client client;

    public void updateInternal(String serverUrl, String key, String value, LocalDateTime now) {
        log.info("updateInternal start. serverUrl: {}, key: {}, value: {}, now:{}",
                serverUrl, key, value, now);
        final String uri = serverUrl + "/internal/update";

        InternalUpdateRequest request = new InternalUpdateRequest(key, value, now);
        try {
            client
                    .target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(request));
        } catch (Exception e) {
            log.error("updateInternal failed. serverUrl: " + serverUrl + ", key: " + key + ", value=" + value, e);
        }
        log.info("updateInternal end.");

    }

    public void deleteInternal(String serverUrl, String key) {
        log.info("deleteInternal start. serverUrl: {}, key: {}",
                serverUrl, key);
        final String uri = serverUrl + "/internal/" + key;
        try {
            client
                    .target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .delete();
        } catch (Exception e) {
            log.error("deleteInternal failed. serverUrl: " + serverUrl + ", key: " + key, e);
        }
        log.info("deleteInternal end.");
    }

    public void updateServer(String serverUrl, String key, String value) {
        log.info("updateServer start. serverUrl: {}, key: {}, value: {}",
                serverUrl, key, value);
        final String uri = serverUrl + "/internal/server";

        UpdateRequest request = new UpdateRequest(key, value);
        try {
            client
                    .target(uri)
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.json(request));
        } catch (Exception e) {
            log.error("updateServer failed. serverUrl: " + serverUrl + ", key: " + key + ", value=" + value, e);
        }
        log.info("updateServer end.");
    }
}
