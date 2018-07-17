package it.devchallenge.service;

import it.devchallenge.model.DataItem;
import it.devchallenge.model.DataItemValue;
import it.devchallenge.model.UpdateValueRequest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface DataItemService {
    void updateValue(String key, UpdateValueRequest request);

    Mono<DataItemValue> getValue(String key);

    Flux<DataItemValue> getHistory(String key);

    void remove(String key, String author);

    Flux<DataItem> dump();
}
