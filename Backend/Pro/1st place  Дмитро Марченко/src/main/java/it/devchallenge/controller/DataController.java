package it.devchallenge.controller;

import it.devchallenge.model.DataItem;
import it.devchallenge.model.DataItemValue;
import it.devchallenge.model.UpdateValueRequest;
import it.devchallenge.service.DataItemService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@AllArgsConstructor
@Slf4j
public class DataController {

    private final DataItemService dataItemService;

    @GetMapping("/data/{key}")
    public Mono<DataItemValue> getValue(@PathVariable("key") String key) {
        log.info("Getting value for key: {}", key);
        return dataItemService.getValue(key);
    }

    @GetMapping("/data/history/{key}")
    public Flux<DataItemValue> getHistory(@PathVariable("key") String key) {
        log.info("Getting history for key: {}", key);
        return dataItemService.getHistory(key);
    }

    @PostMapping("/data/{key}")
    public void update(@PathVariable("key") String key, @RequestBody UpdateValueRequest request) {
        log.info("Updating key {} with request {}", key, request);
        dataItemService.updateValue(key, request);
    }

    @DeleteMapping("/data/{key}")
    public void remove(@PathVariable("key") String key, @RequestParam("author") String author) {
        log.info("Removing value for key: {}, author: {}", key, author);
        dataItemService.remove(key, author);
    }

    @GetMapping("/data/dump")
    public Flux<DataItem> dump(@RequestParam("fileName") String fileName) {
        log.info("Requesting full dump to file: {}", fileName); //TODO: Set up content disposition
        return dataItemService.dump();
    }
}
