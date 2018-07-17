package it.devchallenge.distdb.resource;


import io.swagger.annotations.Api;
import it.devchallenge.distdb.service.UpdateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@Slf4j
@Path("/key")
@Api("/key")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class KeyResource {
    private final UpdateService service;

    @GET
    @Path("/{key}")
    public String getLatestValue(@PathParam("key") String key) {
        log.info("getLatestValue start. key: {}", key);
        String value = service.getLatestValue(key);
        log.info("getLatestValue end. result: {}", value);
        return value;
    }


    @DELETE
    @Path("/{key}")
    public void deleteKey(@PathParam("key") String key) {
        log.info("deleteKey start. key: {}", key);
        service.delete(key);
        log.info("deleteKey end.");
    }
}
