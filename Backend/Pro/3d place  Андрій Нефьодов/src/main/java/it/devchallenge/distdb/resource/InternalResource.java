package it.devchallenge.distdb.resource;

import io.swagger.annotations.Api;
import it.devchallenge.distdb.api.InternalUpdateRequest;
import it.devchallenge.distdb.api.UpdateRequest;
import it.devchallenge.distdb.service.DbService;
import it.devchallenge.distdb.service.InternalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.util.UUID;

@Slf4j
@Path("/internal")
@Api("/internal")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class InternalResource {
    private final InternalService service;
    private final DbService dbService;


    @DELETE
    @Path("/{key}")
    public void deleteKey(@PathParam("key") String key) {
        log.info("deleteKey start. key: {}", key);
        service.delete(key);
        log.info("deleteKey end.");
    }

    @POST
    @Path("/update")
    public void update(InternalUpdateRequest updateRequest) {
        log.info("update start. updateRequest {}", updateRequest);
        service.update(updateRequest);
        log.info("update end.");
    }

    @POST
    @Path("/server")
    public void updateServer(UpdateRequest updateRequest) {
        log.info("updateServer start. updateRequest {}", updateRequest);
        dbService.updateServer(UUID.fromString(updateRequest.getKey()), updateRequest.getValue());
        log.info("updateServer end.");
    }
}
