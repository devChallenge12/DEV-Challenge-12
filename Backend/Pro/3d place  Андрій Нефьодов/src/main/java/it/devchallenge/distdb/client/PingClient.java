package it.devchallenge.distdb.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import java.time.Duration;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NO_CONTENT;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class PingClient {
    private final Client client;

    public Optional<Integer> ping(String url) {
        try {
            final long startTime = System.nanoTime();
            final Response response = client.target(url).request().get();
            final int status = response.getStatus();
            if ((status != OK.getStatusCode()) && (status != NO_CONTENT.getStatusCode())) {
                return Optional.empty();
            }
            final long duration = System.nanoTime() - startTime;
            final long toMillis = Duration.ofNanos(duration).toMillis();
            return Optional.of((int) toMillis);
        } catch (Exception e) {
            log.error("Exception during ping url: " + url , e);
            return Optional.empty();
        }
    }
}
