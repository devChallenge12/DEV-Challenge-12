package it.devchallenge.distdb.service;

import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.dao.Dao;
import it.devchallenge.distdb.dao.ServerDao;
import it.devchallenge.distdb.domain.Server;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class DbService {
    private final Dao dao;
    private final UUID serverId;
    private final ServerDao serverDao;

    @Inject
    public DbService(
                     ServerDao serverDao,
                     Dao dao,
                     @Named("servers") Map<UUID, String> servers,
                     @Named("serverId") String serverId) {
        this.serverId = UUID.fromString(serverId);
        this.serverDao = serverDao;
        addServersFromConfig(servers);
        this.dao = dao;
    }

    private void addServersFromConfig(Map<UUID, String> servers) {
        servers.forEach(serverDao::update);
    }



    public List<Server> getServers() {
        final List<Server> servers = serverDao.getServers();
        log.debug("getServers.  servers {}", servers);
        return servers;
    }

    public Map<UUID, String>  getServersAsMap() {
        final Map<UUID, String> servers  = serverDao.getServersAsMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        log.debug("getServersAsMap.  servers {}", servers);
        return servers;
    }

    public void onboard(OnboardRequest onboardRequest) {
        serverDao.update(onboardRequest.getServerId(), onboardRequest.getServerUrl());
    }

    public void updateServer(UUID uuid, String value) {
        serverDao.update(uuid, value);
    }
}
