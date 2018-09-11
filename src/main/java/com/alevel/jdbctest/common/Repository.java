package com.alevel.jdbctest.common;

import java.util.List;

public interface Repository<T, ID> {

    void save(T entity) throws StorageException;

    List<T> list() throws StorageException;

    T get(ID id) throws StorageException;

    void delete(T entity) throws StorageException;



}
