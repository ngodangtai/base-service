package com.company.module.base.lock;

import com.company.module.common.Constants;
import com.company.module.common.ELockType;

import java.lang.annotation.*;

/**
 * Annotate a method that needs to be processed threaded-safe across
 * instances</br>
 * </br>
 * Lock key is processed in the following order of precedence:
 * <ol>
 * <li>key</li>
 * <li>evalKey</li>
 * </ol>
 *
 * [Warning]: Exception thrown in annotated method should be considered
 *
 * @see Lockable
 * @see DistributedLockService
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequireLock {

    ELockType lockType() default ELockType.REDIS;

    String key() default Constants.Character.EMPTY;

    String evalKey() default Constants.Character.EMPTY;
}
