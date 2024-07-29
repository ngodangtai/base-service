package com.company.module.base.lock;

import com.company.module.common.ELockType;

public interface Locker {

    ELockType getType();

    <T> T process( LockInvoker<T> invoker) throws Throwable;
}
