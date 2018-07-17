package it.devchallenge.distdb.client;

import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.client.JerseyClientConfiguration;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import it.devchallenge.distdb.config.DistDbConfiguration;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.Client;
import java.time.LocalDateTime;

public class ReplicationClientTest {
    private static final String CONFIG_FILE = "distdb_test.yaml";

    private static final DropwizardTestSupport<DistDbConfiguration> SUPPORT =
            new DropwizardTestSupport<>(it.devchallenge.distdb.DistDbApplication.class, ResourceHelpers.resourceFilePath(CONFIG_FILE));
    public static final String SERVER_URL = "http://localhost:8080";
    private static final String VALUE = "2";
    private static final String KEY = "1";
    private static Client CLIENT;


    @BeforeClass
    public static void beforeClass() {
        SUPPORT.before();
        JerseyClientConfiguration clientConfig = new JerseyClientConfiguration();
        clientConfig.setGzipEnabled(false);
        clientConfig.setGzipEnabledForRequests(false);
        CLIENT = new JerseyClientBuilder(SUPPORT.getEnvironment()).using(clientConfig).build("test CLIENT");

    }

    @AfterClass
    public static void afterClass() {
        SUPPORT.after();
    }



    @Test
    public void shouldUpdateInternal() {
        final ReplicationClient replicationClient = new ReplicationClient(CLIENT);
        replicationClient.updateInternal(SERVER_URL, KEY, VALUE, LocalDateTime.now());
    }


}
