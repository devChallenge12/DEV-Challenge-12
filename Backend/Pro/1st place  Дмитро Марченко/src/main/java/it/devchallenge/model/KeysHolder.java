package it.devchallenge.model;

import com.couchbase.client.java.repository.annotation.Field;
import com.couchbase.client.java.repository.annotation.Id;
import lombok.Data;
import org.springframework.data.couchbase.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Document
public class KeysHolder {
    public static final String KEY_NAME = "_allKeys";
    @Id
    private String name;
    @Field
    private Set<String> keys;

    public KeysHolder() {
        this.name = KEY_NAME;
        this.keys = new HashSet<>();
    }
}
