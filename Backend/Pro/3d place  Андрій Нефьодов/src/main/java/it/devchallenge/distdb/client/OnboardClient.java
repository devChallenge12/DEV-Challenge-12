package it.devchallenge.distdb.client;

import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.api.OnboardResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OnboardClient {
    private final Client client;
    public OnboardResponse onboard(String serverToConnectUrl, UUID myServerId, String myServerUrl) {
        log.info("onboard start. serverToConnectUrl: {}, myServerId: {}, myServerUrl: {}",
                serverToConnectUrl, myServerId, myServerUrl);
        final OnboardRequest request = new OnboardRequest(myServerId, myServerUrl);
        Response response =
                client.target(serverToConnectUrl + "/onboard")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.json(request));
        try {

            if (response.getStatus() != Response.Status.OK.getStatusCode()) {
                throw new WebApplicationException("onBoarding failed");
            }
            final OnboardResponse onboardResponse = response.readEntity(OnboardResponse.class);
            log.info("onboard end. response: {}", onboardResponse);
            return onboardResponse;
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
