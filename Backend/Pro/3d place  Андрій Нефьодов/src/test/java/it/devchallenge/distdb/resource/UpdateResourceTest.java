package it.devchallenge.distdb.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import it.devchallenge.distdb.api.UpdateRequest;
import it.devchallenge.distdb.service.UpdateService;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class UpdateResourceTest {


    private static final UpdateService UPDATE_SERVICE = mock(UpdateService.class);
    private static final UpdateResource RESOURCE = new UpdateResource(UPDATE_SERVICE);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .setRegisterDefaultExceptionMappers(false)
            .addProvider(MultiPartFeature.class)
            .addResource(RESOURCE)
            .build();

    @Test
    public void shouldUpdate() {

        UpdateRequest request = new UpdateRequest("1", "2");

        final Response response =
                resources.client().register(MultiPartFeature.class).target("/update")
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .post(Entity.json(request));
        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Response.Status.NO_CONTENT.getStatusCode());
        verify(UPDATE_SERVICE).update(eq(request));
    }
}
