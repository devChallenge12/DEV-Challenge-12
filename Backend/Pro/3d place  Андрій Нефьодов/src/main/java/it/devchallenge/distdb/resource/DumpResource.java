package it.devchallenge.distdb.resource;


import io.swagger.annotations.Api;
import it.devchallenge.distdb.api.DumpResponse;
import it.devchallenge.distdb.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Slf4j
@Path("/dump")
@Api("/dump")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class DumpResource {
    private final UpdateService service;

    @GET
    public DumpResponse getDump() {
        log.info("getDump started.");
        DumpResponse response = new DumpResponse(service.dump());
        log.info("getDump end. response: {}", response);
        return response;
    }
}
