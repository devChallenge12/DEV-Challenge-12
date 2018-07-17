package it.devchallenge.distdb.resource;

import io.dropwizard.testing.junit.ResourceTestRule;
import it.devchallenge.distdb.service.UpdateService;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class KeyResourceTest {
    private static final UpdateService UPDATE_SERVICE = mock(UpdateService.class);
    private static final KeyResource RESOURCE = new KeyResource(UPDATE_SERVICE);
    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .setRegisterDefaultExceptionMappers(false)
            .addProvider(MultiPartFeature.class)
            .addResource(RESOURCE)
            .build();

    private static final String KEY = "1";
    private static final String VALUE = "value1";

    @Test
    public void shouldGetLatestValue()  {
        when(UPDATE_SERVICE.getLatestValue(eq(KEY))).thenReturn(VALUE);
        final Response response = resources.client().target("/key/"+KEY).request(MediaType.APPLICATION_JSON_TYPE).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(String.class)).isEqualTo(VALUE);

    }
}
