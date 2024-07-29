package com.company.module.base.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Primary
@Repository(value = "localCacheRepository")
public class LocalCacheRepository implements CacheRepository {

    private final Map<String, Object> localCache = Collections.synchronizedMap(new WeakHashMap<>());

    public boolean save(String key, Object value) {
        try {
            synchronized (localCache) {
                localCache.put(key, value);
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        try {
            return (V) localCache.get(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public void delete(String key) {
        try {
            localCache.remove(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void delete(Collection<String> keys) {
        keys.forEach(this::delete);
    }
}