package com.company.module.base.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.*;

@Slf4j
@Repository(value = "redisCacheRepository")
public class RedisCacheRepository implements CacheRepository, RedisRepository {

    @Autowired
    @Qualifier(value = "jedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public boolean save(String key, Object hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    public boolean save(String key, Map<Object, Object> values) {
        try {
            redisTemplate.opsForHash().putAll(key, values);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <V> V getHash(String key, Object hashKey) {
        try {
            return (V) redisTemplate.opsForHash().get(key, hashKey);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <HK, V> Map<HK, V> getHash(String key) {
        try {
            return (Map<HK, V>) redisTemplate.opsForHash().entries(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public <HK, V> Map<HK, V> get(String key, Collection<HK> hashKeys) {
        if (CollectionUtils.isEmpty(hashKeys)) {
            return getHash(key);
        }
        try {
            List<V> entries = (List<V>) redisTemplate.opsForHash().multiGet(key, (Collection<Object>) hashKeys);
            if (entries.isEmpty() || entries.size() != hashKeys.size()) {
                return null;
            }
            Map<HK, V> result = new HashMap<>();
            Iterator<V> iterator = entries.iterator();
            for (HK hashKey : hashKeys) {
                result.put(hashKey, iterator.next());
            }
            return result;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public boolean save(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(String key) {
        try {
            return (V) redisTemplate.opsForValue().get(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void delete(Collection<String> keys) {
        try {
            redisTemplate.delete(keys);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void deleteByPattern(String pattern) {
        Set<String> keys = findKeys(pattern);
        if (!CollectionUtils.isEmpty(keys)) {
            delete(keys);
        }
    }

    public void deleteByPattern(Collection<String> patterns) {
        if (CollectionUtils.isEmpty(patterns)) {
            return;
        }
        Set<String> allKeys = new HashSet<>();
        for (String pattern : patterns) {
            Set<String> keys = findKeys(pattern);
            if (!CollectionUtils.isEmpty(keys)) {
                allKeys.addAll(keys);
            }
        }
        if (!CollectionUtils.isEmpty(allKeys)) {
            delete(allKeys);
        }
    }

    public Set<String> findKeys(String pattern) {
        try {
            return redisTemplate.keys(pattern);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return null;
        }
    }
}