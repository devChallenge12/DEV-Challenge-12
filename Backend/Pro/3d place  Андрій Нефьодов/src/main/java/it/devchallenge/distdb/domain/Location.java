package it.devchallenge.distdb.domain;


import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Location {
    private final long descriptorId;
    private final UUID serverId;
    private final long locationId;

    public Location(long descriptorId, UUID serverId) {
        this.descriptorId = descriptorId;
        this.serverId = serverId;
        this.locationId = 0L;
    }

    public Location(Location toInsert, long locationId) {
        this.descriptorId = toInsert.getDescriptorId();
        this.serverId = toInsert.getServerId();
        this.locationId = locationId;
    }
}
