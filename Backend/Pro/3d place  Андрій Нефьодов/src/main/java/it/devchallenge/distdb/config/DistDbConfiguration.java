package it.devchallenge.distdb.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.client.JerseyClientConfiguration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;


public class DistDbConfiguration extends DbConfiguration  {


    @Valid
    private JerseyClientConfiguration jerseyClientConfiguration = new JerseyClientConfiguration();

    @Valid
    @NotNull
    private SwaggerBundleConfiguration swaggerBundleConfiguration;

    @NotNull
    @Getter
    @JsonProperty("storage")
    private String storage;

    @NotNull
    @Getter
    @JsonProperty("serverId")
    private UUID serverId;

    @NotNull
    @Getter
    @JsonProperty("replicationFactor")
    private Integer replicationFactor;

    @NotNull
    @Getter
    @JsonProperty("servers")
    private Map<UUID, String> servers;


    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwaggerBundleConfiguration() {
        return swaggerBundleConfiguration;
    }


    @JsonProperty("jerseyClient")
    public JerseyClientConfiguration getJerseyClientConfiguration() {
        return jerseyClientConfiguration;
    }
}