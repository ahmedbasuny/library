package com.library.domain.validation.isbn;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class IsbnFormatValidator implements ConstraintValidator<IsbnFormat, String> {

    private static final String ISBN_13_REGEX = "^(978|979)\\d{10}$";
    private static final String ISBN_10_REGEX = "^\\d{9}[0-9X]$";

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        if (isbn == null) {
            return false;
        }
        boolean matchesIsbn13 = Pattern.matches(ISBN_13_REGEX, isbn);
        boolean matchesIsbn10 = Pattern.matches(ISBN_10_REGEX, isbn);

        return matchesIsbn13 || matchesIsbn10;
    }
}