package it.devchallenge.repository;


import it.devchallenge.model.KeysHolder;
import org.springframework.data.couchbase.repository.ReactiveCouchbaseRepository;

public interface KeysHolderRepository extends ReactiveCouchbaseRepository<KeysHolder, String> {
}
