package com.company.module.base.repository;

import java.util.*;

public interface CacheRepository {

    boolean save(String key, Object value);
    <V> V get(String key);
    void delete(String key);
    void delete(Collection<String> keys);
}