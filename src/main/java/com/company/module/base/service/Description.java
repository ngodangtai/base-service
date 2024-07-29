package com.company.module.base.service;

import com.company.module.common.Constants;
import com.company.module.common.EFieldType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Description {
    String value() default Constants.Character.EMPTY;
    EFieldType type() default EFieldType.INPUT;
    boolean required() default false;
    int maxLength() default Integer.MAX_VALUE;
    int minLength() default 0;
    boolean isUnique() default false;
    String defaultValue() default Constants.Character.EMPTY;
    String pattern() default Constants.Character.EMPTY;
}
