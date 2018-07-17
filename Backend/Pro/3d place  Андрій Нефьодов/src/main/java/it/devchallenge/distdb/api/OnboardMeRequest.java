package it.devchallenge.distdb.api;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;


@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public class OnboardMeRequest {
    private final String serverToConnectUrl;
    private final String myServerUrl;
}
