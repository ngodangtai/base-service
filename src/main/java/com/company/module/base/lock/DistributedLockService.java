package com.company.module.base.lock;

import com.company.module.common.Constants;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
import com.company.module.utils.Utilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * Execute a required-locked method
 *
 * @see RequireLock
 *
 */
@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class DistributedLockService {

    private final LockerFactory factory;

    @Around("@annotation(requireLock)")
    public Object lock(ProceedingJoinPoint pjp, RequireLock requireLock) throws Throwable {
        final String key = getKey(pjp, requireLock);
        if (ObjectUtils.isEmpty(key)) {
            log.error("No lock key found!");
            throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, new StringBuilder("process redisLock error, key: "));
        }
        log.info("Acquired lock key {}", key);
        Locker locker = factory.getLocker(requireLock.lockType());
        return locker.process(new LockInvoker<>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Object invoke() throws Throwable {
                return pjp.proceed();
            }
        });
    }

    private String getKey(ProceedingJoinPoint pjp, RequireLock requireLock) {
        return requireLock.key() == null ? StringUtils.EMPTY : requireLock.key()
                + Utilities.firstSatisfied(StringUtils::isNotBlank,
                () -> extractKeyFromArgs(pjp.getArgs()),
                () -> Utilities.extractKeyFromSpel(pjp, requireLock.evalKey()).toString());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private String extractKeyFromArgs(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof Lockable lockable && StringUtils.isNotBlank(lockable.getLockKey())) {
                return lockable.getLockKey();
            }
            if (!(arg instanceof List list) || list.isEmpty()) {
                continue;
            }
            List<Object> items = (List) arg;
            if (items.get(0) instanceof Lockable lockable && StringUtils.isNotBlank(lockable.getLockKey())) {
                return lockable.getLockKey();
            }
        }
        return Constants.Character.EMPTY;
    }
}
