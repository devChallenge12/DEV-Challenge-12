package it.devchallenge.distdb.dao;

import it.devchallenge.distdb.domain.Detail;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static it.devchallenge.distdb.service.OnboardService.START_OF_TIME;
import static java.time.LocalDateTime.now;

public class Dao {
    private final Map<String, Detail> data = new HashMap<>();

    public void update(String key, String value) {
        data.put(key, new Detail(value, now()));
    }

    public String getLatestValue(String key) {
        return data.get(key).getValue();
    }

    public void delete(String key) {
        data.remove(key);
    }

    public Map<String, String> dump() {
        return data.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
    }

    public LocalDateTime getLastUpdated() {
        final LocalDateTime[] max = {START_OF_TIME};
        Consumer<? super Detail> maxCalc = d -> {
            if (d.getUpdated().isAfter(max[0])) {
                max[0] = d.getUpdated();
            }
        };
        data.values().forEach(maxCalc);
        return max[0];
    }

    public Map<String, String> getRecordsUpdatedSince(LocalDateTime lastUpdate) {
        return data.entrySet().stream()
                .filter(e -> e.getValue().getUpdated().isAfter(lastUpdate))
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().getValue()));
    }

    public Detail getLatestData(String key) {
        return data.get(key);
    }
}
