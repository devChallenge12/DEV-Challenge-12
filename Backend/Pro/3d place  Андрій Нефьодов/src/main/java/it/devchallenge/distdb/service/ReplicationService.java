package it.devchallenge.distdb.service;

import it.devchallenge.distdb.client.ReplicationClient;
import it.devchallenge.distdb.domain.Server;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Singleton
@Slf4j
public class ReplicationService {
    private final UUID serverId;
    private final ReplicationClient client;
    private final DbService dbService;

    @Inject
    ReplicationService(ReplicationClient client,
                       DbService dbService,
                       @Named("servers") Map<UUID, String> servers,
                       @Named("serverId") String serverId
    ) {
        this.client = client;
        this.serverId = UUID.fromString(serverId);
        this.dbService = dbService;
        populateServerUrls(servers);
    }

    private void populateServerUrls(Map<UUID, String> servers) {
        servers.forEach(dbService::updateServer);
    }


    private List<String> getServerUrlsForReplication() {
        List<Server> servers = dbService.getServers();
        final List<String> result = servers.stream()
                .filter(e -> !e.getServerId().equals(serverId))
                .map(Server::getUrl)
                .collect(Collectors.toList());
        log.debug("getServerUrlsForReplication end. result: {}", result);
        return result;
    }



    public void update(String key, String value, LocalDateTime now) {
        List<String> serverUrls = getServerUrlsForReplication();
        for (String serverUrl : serverUrls) {
            CompletableFuture.runAsync(()->client.updateInternal(serverUrl, key, value, now));
        }
    }

    public void delete(String key) {
        List<String> serverUrls = getServerUrlsForReplication();
        for (String serverUrl : serverUrls) {
            CompletableFuture.runAsync(()->client.deleteInternal(serverUrl, key));
        }
    }


    public void updateServer(String key, String value) {
        List<String> serverUrls = getServerUrlsForReplication();
        for (String serverUrl : serverUrls) {
            CompletableFuture.runAsync(()->client.updateServer(serverUrl, key, value));
        }
    }
}
