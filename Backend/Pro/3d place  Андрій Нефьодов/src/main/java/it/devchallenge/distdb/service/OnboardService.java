package it.devchallenge.distdb.service;

import it.devchallenge.distdb.api.OnboardMeRequest;
import it.devchallenge.distdb.api.OnboardRequest;
import it.devchallenge.distdb.api.OnboardResponse;
import it.devchallenge.distdb.client.OnboardClient;
import it.devchallenge.distdb.client.RestoreClient;
import it.devchallenge.distdb.dao.Dao;
import it.devchallenge.distdb.domain.Detail;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Named;
import javax.ws.rs.WebApplicationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
public class OnboardService {

    public static final LocalDateTime START_OF_TIME = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
    private final UUID serverId;
    private UpdateService updateService;
    private DbService dbService;
    private ReplicationService replicationService;
    private Dao dao;
    private RestoreClient restoreClient;

    @Inject
    OnboardService(OnboardClient onboardClient,
                   UpdateService updateService,
                   DbService dbService,
                   Dao dao,
                   ReplicationService replicationService,
                   @Named("serverId") String serverId
    ) {
        this.onboardClient = onboardClient;
        this.updateService = updateService;
        this.dbService = dbService;
        this.replicationService = replicationService;
        this.dao = dao;
        this.serverId = UUID.fromString(serverId);
    }

    private final OnboardClient onboardClient;

    public void onboardMe(OnboardMeRequest onboardMeRequest) {
        final OnboardResponse onboardResponse = onboardClient
                .onboard(onboardMeRequest.getServerToConnectUrl(), serverId, onboardMeRequest.getMyServerUrl());
        processUpdatedRecords(onboardResponse.getDump());
        addAllServers(onboardResponse.getServers());
    }

    public OnboardResponse onboard(OnboardRequest onboardRequest) {
        final Map<String, String> dump = updateService.dump();
        final Map<UUID, String> servers = dbService.getServersAsMap();
        replicationService.updateServer(onboardRequest.getServerId().toString(), onboardRequest.getServerUrl());
        dbService.onboard(onboardRequest);
        return new OnboardResponse(dump, servers);
    }

    public void restoreMe() {
        LocalDateTime lastUpdated = getLastUpdated();
        OnboardResponse onboardResponse = askTillSomeResponds(lastUpdated);
        processUpdatedRecords(onboardResponse.getDump());
        addAllServers(onboardResponse.getServers());
    }

    private OnboardResponse askTillSomeResponds(LocalDateTime lastUpdated) {
        for (String server : getListOfServersToAskForRestore()) {
            try {
                final OnboardResponse response = restoreClient.restore(server, lastUpdated);
                if (response != null) {
                    return response;
                }
            } catch (Exception e) {
                log.warn("askTillSomeResponds error", e);
            }
        }
        throw new WebApplicationException("failed to restore");
    }

    private List<String> getListOfServersToAskForRestore() {
        return dbService.getServersAsMap().entrySet().stream().filter(e -> !e.getKey().equals(serverId))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    private void addAllServers(Map<UUID, String> servers) {
        servers.entrySet().forEach(e -> dbService.updateServer(e.getKey(), e.getValue()));
    }

    private void processUpdatedRecords(Map<String, String> dump) {
        dump.entrySet().forEach(e -> dao.update(e.getKey(), e.getValue()));
    }

    private LocalDateTime getLastUpdated() {
        LocalDateTime lastUpdated = START_OF_TIME;
        LocalDateTime lastUpdatedFromDao = dao.getLastUpdated();
        if ((lastUpdatedFromDao != null) && (lastUpdated.isBefore(lastUpdatedFromDao))) {
            lastUpdated = lastUpdatedFromDao;
        }
        return lastUpdated;
    }

    public OnboardResponse restore(LocalDateTime lastUpdate) {
        final Map<String, String> dump = dao.getRecordsUpdatedSince(lastUpdate);
        Map<UUID, String> servers = dbService.getServersAsMap();
        return new OnboardResponse(dump, servers);
    }
}
