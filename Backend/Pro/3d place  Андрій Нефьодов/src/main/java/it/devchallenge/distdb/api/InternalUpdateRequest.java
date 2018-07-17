package it.devchallenge.distdb.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class InternalUpdateRequest {
    private final String key;
    private final String value;
    private final LocalDateTime updated;
}