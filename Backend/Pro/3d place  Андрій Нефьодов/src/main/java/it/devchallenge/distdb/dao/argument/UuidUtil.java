package it.devchallenge.distdb.dao.argument;

import java.nio.ByteBuffer;
import java.util.UUID;

public class UuidUtil {

    public static byte[] toBytes(UUID id) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(id.getMostSignificantBits());
        bb.putLong(id.getLeastSignificantBits());
        return bb.array();
    }

    public static UUID fromBytes(byte[] data) {
        ByteBuffer bb = ByteBuffer.wrap(data);
        return new UUID(bb.getLong(), bb.getLong());
    }

}
