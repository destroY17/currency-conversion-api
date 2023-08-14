package com.destroY17.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository <T, ID> {
    List<T> getAll();
    Optional<T> getById(ID id);
    T save(T entity);
    T update(T entity);
}
