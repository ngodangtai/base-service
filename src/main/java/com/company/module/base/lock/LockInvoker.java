package com.company.module.base.lock;

public interface LockInvoker<T> {

    String getKey();

    T invoke() throws Throwable;
}
