package it.devchallenge.distdb.resource;

import io.swagger.annotations.Api;
import it.devchallenge.distdb.api.UpdateRequest;
import it.devchallenge.distdb.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@Path("/update")
@Api("/update")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UpdateResource {

    private final UpdateService service;

    @POST
    public void update(UpdateRequest updateRequest) {
        log.info("update start. updateRequest {}", updateRequest);
        service.update(updateRequest);
        log.info("update end.");
    }
}
