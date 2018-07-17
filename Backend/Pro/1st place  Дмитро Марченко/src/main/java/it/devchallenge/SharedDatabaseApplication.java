package it.devchallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

@SpringBootApplication
@EnableCouchbaseRepositories
public class SharedDatabaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(SharedDatabaseApplication.class, args);
    }
}