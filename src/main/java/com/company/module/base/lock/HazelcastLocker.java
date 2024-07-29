package com.company.module.base.lock;

import com.company.module.common.ELockType;
import com.company.module.exception.BaseException;
import com.company.module.exception.ErrorCode;
//import com.hazelcast.core.HazelcastInstance;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class HazelcastLocker implements Locker {

    @Value("${spring.hazelcast.lock.wait-time:3000}")
    private int waitTime;

//    @Qualifier("hazelcastInstance")
//    private final HazelcastInstance hazelcastInstance;

    @Override
    public ELockType getType() {
        return ELockType.HAZELCAST;
    }

    @Override
    public <T> T process(LockInvoker<T> invoker) throws Throwable {
//        var lock = hazelcastInstance.getCPSubsystem().getLock(invoker.getKey());
//        if (lock.tryLock(waitTime, TimeUnit.SECONDS)) {
//            try {
//                return invoker.invoke();
//            } finally {
//                lock.unlock();
//            }
//        }
        throw new BaseException(ErrorCode.INTERNAL_SERVER_ERROR, new StringBuilder("HazelcastLocker error, key: ")
                .append(invoker.getKey()));
    }

}
