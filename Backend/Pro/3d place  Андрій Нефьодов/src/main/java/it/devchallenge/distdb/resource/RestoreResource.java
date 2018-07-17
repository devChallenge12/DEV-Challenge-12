package it.devchallenge.distdb.resource;

import io.swagger.annotations.Api;
import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.api.OnboardResponse;
import it.devchallenge.distdb.service.DbService;
import it.devchallenge.distdb.service.OnboardService;
import it.devchallenge.distdb.service.UpdateService;
import it.devchallenge.distdb.util.FromStringConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Path("/restore")
@Api("/restore")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class RestoreResource {
    private final OnboardService onboardService;

    @GET
    @Path("/{lastUpdate}")
    public OnboardResponse restore(@PathParam("lastUpdate") String lastUpdate) {
        log.info("restore start. lastUpdate: {}", lastUpdate);
        final OnboardResponse onboardResponse = onboardService.restore(FromStringConverter.getLocalDateTime
                (lastUpdate));
        log.info("restore end. response: {}",  onboardResponse);
        return onboardResponse;
    }

    @POST
    @Path("/me")
    public void restoreMe() {
        log.info("restoreMe start");
        onboardService.restoreMe();
        log.info("restoreMe end");
    }
}
