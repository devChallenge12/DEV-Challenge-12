package it.devchallenge.distdb;

import it.devchallenge.distdb.client.OnboardClient;
import it.devchallenge.distdb.client.ReplicationClient;
import it.devchallenge.distdb.config.DistDbConfiguration;
import it.devchallenge.distdb.dao.Dao;
import it.devchallenge.distdb.dao.ServerDao;
import it.devchallenge.distdb.service.DbService;
import it.devchallenge.distdb.service.InternalService;
import it.devchallenge.distdb.service.OnboardService;
import it.devchallenge.distdb.service.ReplicationService;
import it.devchallenge.distdb.service.UpdateService;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.skife.jdbi.v2.DBI;

import javax.inject.Singleton;
import javax.ws.rs.client.Client;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

public class DistDbBinder extends AbstractBinder {

    private final DBI dbi;
    private final DistDbConfiguration conf;
    private final Client client;

    public DistDbBinder(DistDbConfiguration conf, DBI dbi, Client client) {
        this.dbi = dbi;
        this.conf = conf;
        this.client = client;
    }

    @Override
    protected void configure() {
        bind(client).to(Client.class);

        bind(dbi).to(DBI.class);
        bind(conf).to(DistDbConfiguration.class);

        bindNamedString("storage", conf.getStorage());
        bindNamedString("serverId", conf.getServerId().toString());
        TypeLiteral<Map<UUID, String>> type = new TypeLiteral<Map<UUID, String>>() {};
        bind(conf.getServers()).named("servers").to(type);
        bind(conf.getReplicationFactor()).named("replicationFactor").to(Integer.class);

        bindSingleton(Dao.class);
        bindSingleton(ServerDao.class);
        bindSingleton(UpdateService.class);
        bindSingleton(InternalService.class);
        bindSingleton(OnboardService.class);

        bindSingleton(ReplicationClient.class);
        bindSingleton(OnboardClient.class);
        bindSingleton(ReplicationService.class);
        bindSingleton(DbService.class);
    }

    private void bindSingleton(Class<?>... classes) {
        for (Class<?> clazz : classes) {
            bindAsContract(clazz).in(Singleton.class);
        }
    }

    private void bindNamedString(String name, String value) {
        bind(value).named(name).to(String.class);
    }

    private void bindDynamicDao(Class<?>... types) {
        Arrays.asList(types).forEach(this::bindDynamicDao);
    }

    private <T> void bindDynamicDao(Class<T> sqlObjectType) {
        bind(dbi.onDemand(sqlObjectType)).to(sqlObjectType);
    }

}
