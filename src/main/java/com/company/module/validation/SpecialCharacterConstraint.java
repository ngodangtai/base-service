package com.company.module.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SpecialCharacterValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface SpecialCharacterConstraint {

    String message() default "Input cannot contain special characters";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
