package it.devchallenge.distdb.dao.argument;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;
import org.skife.jdbi.v2.tweak.ArgumentFactory;

import java.util.UUID;

public class UuidBytesArgumentFactory implements ArgumentFactory<UUID> {

    @Override
    public boolean accepts(Class<?> expectedType, Object value, StatementContext ctx) {
        return value instanceof UUID;
    }

    @Override
    public Argument build(Class<?> expectedType, UUID value, StatementContext ctx) {
        byte[] byteValue = UuidUtil.toBytes(value);
        return new UuidBytesArgument(byteValue);
    }
}
