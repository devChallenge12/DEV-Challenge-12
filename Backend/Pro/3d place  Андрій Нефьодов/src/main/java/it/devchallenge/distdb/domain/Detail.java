package it.devchallenge.distdb.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class Detail {
    private final String value;
    private final LocalDateTime updated;
}
