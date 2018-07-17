package it.devchallenge.service;

import it.devchallenge.model.DataItem;
import it.devchallenge.model.DataItemValue;
import it.devchallenge.model.KeysHolder;
import it.devchallenge.model.UpdateValueRequest;
import it.devchallenge.rabbitconfig.RabbitConfig;
import it.devchallenge.repository.DataItemRepository;
import it.devchallenge.repository.KeysHolderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

import static it.devchallenge.model.KeysHolder.KEY_NAME;

@Service
@AllArgsConstructor
@Slf4j
public class DataItemServiceImpl implements DataItemService {

    private static final Comparator<DataItemValue> VALUE_COMPARATOR = Comparator
            .comparing(DataItemValue::getCreationTime);
    private final DataItemRepository dataItemRepository;
    private final KeysHolderRepository keysHolderRepository;
    private final RabbitTemplate rabbitTemplate;

    @Override
    public void updateValue(String key, UpdateValueRequest request) {
        keysHolderRepository.findById(KEY_NAME)
                .defaultIfEmpty(new KeysHolder())
                .map(keysHolder -> addKey(keysHolder, key))
                .flatMap(keysHolderRepository::save)
                .subscribe(keysHolder -> log.info("New key: {} added to database", key));

        dataItemRepository.findById(key)
                .defaultIfEmpty(new DataItem(key))
                .map(dataItem -> addValue(request, dataItem))
                .flatMap(dataItemRepository::save)
                .subscribe(dataItem -> log.info("New value for key '{}' saved. Result: {}", key, dataItem));

        sendUpdateEvent(key, request);
    }

    private void sendUpdateEvent(String key, UpdateValueRequest request) {
        String message = "Key: " + key + " updated to: " + request.getValue();
        rabbitTemplate.convertAndSend(RabbitConfig.TOPIC_EXCHANGE_NAME, RabbitConfig.ROUTING_KEY, message);
    }

    @Override
    public Mono<DataItemValue> getValue(String key) {
        return dataItemRepository.findById(key)
                .map(DataItem::getValues)
                .map(values -> values.stream().max(VALUE_COMPARATOR)) //TODO: Refactor to flux
                .map(optional -> optional.orElse(null));
    }

    @Override
    public Flux<DataItemValue> getHistory(String key) {
        return dataItemRepository.findById(key)
                .map(DataItem::getValues)
                .flatMapMany(Flux::fromIterable);
    }

    @Override
    public void remove(String key, String author) {
        updateValue(key, new UpdateValueRequest(author, null));
    }

    @Override
    public Flux<DataItem> dump() {
        return keysHolderRepository.findById(KEY_NAME)
                .map(KeysHolder::getKeys)
                .flatMapMany(Flux::fromIterable)
                .flatMap(dataItemRepository::findById);
    }

    private static DataItem addValue(UpdateValueRequest request, DataItem dataItem) {
        dataItem.getValues().add(new DataItemValue(request.getAuthor(), request.getValue()));
        return dataItem;
    }

    private static KeysHolder addKey(KeysHolder keysHolder, String key) {
        keysHolder.getKeys().add(key);
        return keysHolder;
    }
}
