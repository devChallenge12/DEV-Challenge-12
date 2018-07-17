package it.devchallenge.distdb.dao.argument;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.Argument;

import java.sql.PreparedStatement;
import java.sql.SQLException;

class UuidBytesArgument implements Argument {

    private final byte[] value;

    UuidBytesArgument(byte[] value) {
        this.value = value;
    }

    @Override
    public void apply(int position, PreparedStatement statement, StatementContext ctx) throws SQLException {
        statement.setBytes(position, value);
    }

}
