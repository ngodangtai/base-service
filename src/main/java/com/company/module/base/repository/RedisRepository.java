package com.company.module.base.repository;

import java.util.*;

public interface RedisRepository {

    boolean save(String key, Object hashKey, Object value);
    boolean save(String key, Map<Object, Object> values);
    <V> V getHash(String key, Object hashKey);
    <HK, V> Map<HK, V> getHash(String key);
    <HK, V> Map<HK, V> get(String key, Collection<HK> hashKeys);
    void deleteByPattern(String pattern);
    void deleteByPattern(Collection<String> patterns);
    Set<String> findKeys(String pattern);
}