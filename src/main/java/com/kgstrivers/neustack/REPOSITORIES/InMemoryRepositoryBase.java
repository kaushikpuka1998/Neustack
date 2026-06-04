package com.kgstrivers.neustack.REPOSITORIES;

import java.util.*;
import java.util.function.Function;


public abstract class InMemoryRepositoryBase<T extends HasId> {

    protected final Map<String, T> storage = new HashMap<>();
    public T save(T entity) {
        storage.put(entity.getId(), entity);
        return entity;
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    public int countAll() {
        return storage.size();
    }
}