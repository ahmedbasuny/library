package com.library.domain.common.validation.isbn;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UniqueIsbnValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsbnUniqueness {
    String message() default "ISBN already exists";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}