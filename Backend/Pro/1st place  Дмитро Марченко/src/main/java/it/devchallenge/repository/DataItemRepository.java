package it.devchallenge.repository;


import it.devchallenge.model.DataItem;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

public interface DataItemRepository extends ReactiveCouchbaseRepository<DataItem, String> {
}
