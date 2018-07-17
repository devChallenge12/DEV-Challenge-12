package it.devchallenge.distdb.resource;

import io.swagger.annotations.Api;
import it.devchallenge.distdb.api.OnboardMeRequest;
import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.api.OnboardResponse;
import it.devchallenge.distdb.service.OnboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Slf4j
@Path("/onboard")
@Api("/onboard")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class OnboardResource {

    private final OnboardService onboardService;

    @POST
    public OnboardResponse onboard(OnboardRequest onboardRequest) {
        log.info("onboard start. request: {}", onboardRequest);
        final OnboardResponse onboardResponse = onboardService.onboard(onboardRequest);
        log.info("onboard end. response: {}",  onboardResponse);
        return onboardResponse;
    }


    @POST
    @Path("/me")
    public void onboardMe(OnboardMeRequest onboardMeRequest) {
        log.info("onboardMe start. request: {}", onboardMeRequest);
        onboardService.onboardMe(onboardMeRequest);
        log.info("onboardMe end.");
    }
}
