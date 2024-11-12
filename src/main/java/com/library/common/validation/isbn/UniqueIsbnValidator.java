package com.library.common.validation.isbn;

import com.library.domain.book.BookService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class UniqueIsbnValidator implements ConstraintValidator<IsbnUniqueness, String> {

    private final BookService bookService;

    @Override
    public boolean isValid(String isbn, ConstraintValidatorContext context) {
        return isbn != null && !bookService.existsByIsbn(isbn);
    }
}