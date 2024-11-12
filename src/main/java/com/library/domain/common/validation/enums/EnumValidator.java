package com.library.domain.common.validation.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.stream.Collectors;

public class EnumValidator implements ConstraintValidator<EnumValidation, String> {

    private Enum<?>[] enumValues;
    private String enumValuesList;

    @Override
    public void initialize(EnumValidation annotation) {
        this.enumValues = annotation.value().getEnumConstants();
        this.enumValuesList = Arrays.stream(enumValues)
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        boolean isValid = false;

        isValid = Arrays.stream(enumValues)
                .anyMatch(e -> e.name().equals(value));

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Invalid value. This field must match one of the following values: [" + enumValuesList + "]"
            ).addConstraintViolation();
        }

        return isValid;
    }
}
