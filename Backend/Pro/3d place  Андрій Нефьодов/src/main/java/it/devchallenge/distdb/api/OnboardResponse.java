package it.devchallenge.distdb.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;
import java.util.UUID;

@Getter
@ToString
@AllArgsConstructor
@EqualsAndHashCode
public class OnboardResponse {
    private final Map<String, String> dump;
    private final Map<UUID, String> servers;
}
