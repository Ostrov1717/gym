package org.example.dao;

import java.util.Map;

public interface Storage {
    <T> void save(Class<T> entityClass, Long id, T entity);
    <T> T findById(Class<T> entityClass, Long id);
    <T> void delete(Class<T> entityClass, Long id);
    <T> Map<Long, T> getAll(Class<T> entityClass);
}
