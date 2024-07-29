package com.company.module.base.lock;

import com.company.module.common.ELockType;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisLocker implements Locker {

    private final RedissonClient redissonClient;

    @Value("${spring.data.redis.wait-time}")
    private Integer waitTime;

    @Value("${spring.data.redis.lease-time}")
    private Integer leaseTime;

    @Override
    public ELockType getType() {
        return ELockType.REDIS;
    }

    @Override
    public <T> T process(LockInvoker<T> invoker) throws Throwable {
        var lock = redissonClient.getLock(invoker.getKey());
        try {
            if (lock.tryLock(waitTime, leaseTime, TimeUnit.MILLISECONDS)) {
                try {
                    return invoker.invoke();
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while locking key {}", invoker.getKey());
        }
        log.error("Unable to acquire lock for key {}", invoker.getKey());
        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, new StringBuilder("RedisLock error, key: ")
                .append(invoker.getKey()));
    }

}
