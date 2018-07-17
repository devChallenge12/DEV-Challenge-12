package it.devchallenge.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@Document
public class DataItem {
    @Id
    @Field
    private String key;
    @Field
    private List<DataItemValue> values;

    public DataItem(String key) {
        this.key = key;
        this.values = new ArrayList<>();
    }
}
