package it.devchallenge.distdb.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Descriptor {
    private final String userName;
    private final String fileName;
    private final int version;
    private final long descriptorId;

    public Descriptor(String userName, String fileName, int version) {
        this.userName = userName;
        this.fileName = fileName;
        this.version = version;
        this.descriptorId = 0L;
    }

    public Descriptor(Descriptor toInsert, long descriptorId) {
        this.userName = toInsert.getUserName();
        this.fileName = toInsert.getFileName();
        this.version = toInsert.getVersion();
        this.descriptorId = descriptorId;
    }
}
