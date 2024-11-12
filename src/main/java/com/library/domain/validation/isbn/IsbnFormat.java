package com.library.domain.validation.isbn;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsbnFormatValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsbnFormat {
    String message() default "Invalid ISBN format. ISBN should be either ISBN-10 or ISBN-13.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
