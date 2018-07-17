package it.devchallenge.distdb.dao;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.configuration.ConfigurationException;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.ConfigurationSourceProvider;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.FileConfigurationSourceProvider;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.lifecycle.setup.LifecycleEnvironment;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.setup.Environment;
import it.devchallenge.distdb.config.DbConfiguration;
import it.devchallenge.distdb.config.DistDbConfiguration;
import org.junit.BeforeClass;
import org.skife.jdbi.v2.DBI;

import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import java.util.concurrent.Executors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public abstract class JdbiIntegrationTest {

    private static final HealthCheckRegistry HEALTH_CHECKS = mock(HealthCheckRegistry.class);
    private static final LifecycleEnvironment LIFECYCLE_ENVIRONMENT = mock(LifecycleEnvironment.class);
    private static final Environment ENVIRONMENT = mock(Environment.class);
    private static final MetricRegistry METRIC_REGISTRY = new MetricRegistry();
    private static final String CONFIG_FILE = "./src/test/resources/distdb_test.yaml";
    static DBI DBI_INSTANCE;

    static {
        BootstrapLogging.bootstrap();
    }

    @BeforeClass
    public static synchronized void beforeClass() throws Exception {
        if (DBI_INSTANCE != null) {
            return;
        }
        when(ENVIRONMENT.healthChecks()).thenReturn(HEALTH_CHECKS);
        when(ENVIRONMENT.lifecycle()).thenReturn(LIFECYCLE_ENVIRONMENT);
        when(ENVIRONMENT.metrics()).thenReturn(METRIC_REGISTRY);
        when(ENVIRONMENT.getHealthCheckExecutorService()).thenReturn(Executors.newSingleThreadExecutor());
        DataSourceFactory datasourceFactory = getDatasourceFactory(CONFIG_FILE, DistDbConfiguration.class);
        DBI_INSTANCE = new DBIFactory().build(ENVIRONMENT, datasourceFactory, "mysql");
    }

    private static <T extends DbConfiguration> DataSourceFactory getDatasourceFactory(String configFile, Class<T>
            configurationClass) throws java.io
            .IOException, ConfigurationException {
        ObjectMapper mapper = Jackson.newObjectMapper();
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        ConfigurationFactoryFactory<T> factoryFactory = new DefaultConfigurationFactoryFactory<>();
        ConfigurationFactory<T> configurationFactory = factoryFactory.create
                (configurationClass,
                validatorFactory.getValidator(), mapper, "login");

        EnvironmentVariableSubstitutor substitutor = new EnvironmentVariableSubstitutor(false);
        ConfigurationSourceProvider configurationSourceProvider = new SubstitutingSourceProvider(
                new FileConfigurationSourceProvider(),
                substitutor
        );
        T loginConfiguration = configurationFactory.build(configurationSourceProvider, configFile);
        return loginConfiguration.getDataSourceFactory();
    }


    <D> D onDemandDao(Class<D> sqlObjectType) {
        return DBI_INSTANCE.onDemand(sqlObjectType);
    }
}