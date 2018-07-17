package it.devchallenge.distdb;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.Application;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jersey.setup.JerseyEnvironment;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import it.devchallenge.distdb.config.DistDbConfiguration;
import it.devchallenge.distdb.resource.UpdateResource;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.skife.jdbi.v2.DBI;

import javax.ws.rs.client.Client;

import static java.util.logging.Logger.getLogger;


public class DistDbApplication extends Application<DistDbConfiguration> {
    private MetricRegistry metricRegistry;

    public static void main(String[] args) throws Exception {
        new it.devchallenge.distdb.DistDbApplication().run(args);
    }

    @Override
    public String getName() {
        return "distdb";
    }

    @Override
    public void initialize(Bootstrap<DistDbConfiguration> bootstrap) {
        initializeConfigSubstitution(bootstrap);

        // Liquibase migrations
        bootstrap.addBundle(new MigrationsBundle<DistDbConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(DistDbConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new SwaggerBundle<DistDbConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(DistDbConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    protected void initializeConfigSubstitution(Bootstrap<DistDbConfiguration> bootstrap) {
        // Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        metricRegistry = bootstrap.getMetricRegistry();
    }

    @Override
    public void run(DistDbConfiguration configuration, Environment environment) {
        Client client = createClient(configuration, environment);
        JerseyEnvironment jersey = environment.jersey();
        jersey.register(loggingFeature("InboundRequestResponse"));

        jersey.packages(UpdateResource.class.getPackage().getName());
        initBinding(configuration, environment, client);
    }

    private LoggingFeature loggingFeature(String loggerName) {
        return new LoggingFeature(getLogger(loggerName), LoggingFeature.Verbosity.PAYLOAD_ANY);
    }

    private void initBinding(final DistDbConfiguration configuration, Environment environment, Client client) {
        DBI dbi = new DBIFactory().build(environment, configuration.getDataSourceFactory(), "h2");

        environment.jersey().register(new DistDbBinder(configuration,dbi, client));
    }


    private Client createClient(DistDbConfiguration configuration, Environment environment) {
        return new JerseyClientBuilder(environment)
                        .using(configuration.getJerseyClientConfiguration())
                .build(getName())
                //for logging outgoing requests
                .register(loggingFeature("OutboundRequestResponse"));
    }

}
