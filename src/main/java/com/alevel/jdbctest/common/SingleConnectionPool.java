package com.alevel.jdbctest.common;

import java.sql.Connection;
import java.util.function.Supplier;

public class SingleConnectionPool implements Supplier<Connection> {

    private final Connection connection;

    public SingleConnectionPool(Connection connection) {
        this.connection = connection;
    }

    @Override
    public Connection get() {
        return connection;
    }

}
