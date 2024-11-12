package com.library.domain.common.validation.enums;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = EnumValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface EnumValidation {

    Class<? extends Enum<?>> value();

    String message() default "Invalid value. This field must match one of the predefined values.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
