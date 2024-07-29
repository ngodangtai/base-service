package com.company.module.base.lock;

import com.company.module.common.ELockType;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class LocalLocker implements Locker {

    private final ConcurrentHashMap<String, LockWrapper> locks = new ConcurrentHashMap<>();

    @Override
    public ELockType getType() {
        return ELockType.LOCAL;
    }

    @Override
    public <T> T process(LockInvoker<T> invoker) throws Throwable {
        LockWrapper lockWrapper;
        synchronized (this.locks) {
            lockWrapper = locks.compute(invoker.getKey(), (k, v) -> v == null ? new LockWrapper() : v.addThreadInQueue());
        }
        lockWrapper.lock.lock();
        try {
            return invoker.invoke();
        } finally {
            synchronized (this.locks) {
                if (lockWrapper.removeThreadFromQueue() == 0) {
                    locks.remove(invoker.getKey(), lockWrapper);
                }
            }
        }
    }

    private static class LockWrapper {
        private final Lock lock = new ReentrantLock();
        private final AtomicInteger numberOfThreadsInQueue = new AtomicInteger(1);

        public LockWrapper addThreadInQueue() {
            numberOfThreadsInQueue.incrementAndGet();
            return this;
        }

        public int removeThreadFromQueue() {
            return numberOfThreadsInQueue.decrementAndGet();
        }
    }

}
