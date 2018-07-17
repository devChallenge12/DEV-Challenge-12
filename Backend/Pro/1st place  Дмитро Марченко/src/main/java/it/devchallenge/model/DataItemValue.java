package it.devchallenge.model;

import com.couchbase.client.java.repository.annotation.Field;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document
public class DataItemValue {
    @Field
    private LocalDateTime creationTime;
    @Field
    private String author;
    @Field
    private String value;

    public DataItemValue(String author, String value) {
        this.author = author;
        this.value = value;
        this.creationTime = LocalDateTime.now();
    }
}
