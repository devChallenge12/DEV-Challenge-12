package it.devchallenge.distdb.api;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class OnboardRequest {
    private final UUID serverId;
    private final String serverUrl;
}
