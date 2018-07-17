package it.devchallenge.distdb.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class DumpResponse {
    private final Map<String, String> dump;
}
