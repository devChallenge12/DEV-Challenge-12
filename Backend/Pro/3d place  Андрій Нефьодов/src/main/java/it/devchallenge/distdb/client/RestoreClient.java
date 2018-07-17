package it.devchallenge.distdb.client;

import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.api.OnboardResponse;
import it.devchallenge.distdb.util.FromStringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestoreClient {
    private final Client client;

    public OnboardResponse restore(String serverToConnectUrl, LocalDateTime lastUpdated) {
        log.info("restore start. serverToConnectUrl: {}, myServerId: {}, myServerUrl: {}");
        Response response =
                client.target(serverToConnectUrl + "/restore/"+ FromStringConverter.from(lastUpdated))
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.json(lastUpdated));
        try {

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new WebApplicationException("restore failed");
            }
            final OnboardResponse onboardResponse = response.readEntity(OnboardResponse.class);
            log.info("restore end. response: {}", onboardResponse);
            return onboardResponse;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

}
