package com.library.domain.book.models;

import com.library.common.validation.isbn.IsbnFormat;
import com.library.common.validation.isbn.IsbnUniqueness;
import jakarta.validation.constraints.*;

public record Book(
        Long id,
        @NotEmpty(message = "Title is required.")
        String title,
        @NotEmpty(message = "Author is required.")
        String author,
        @Max(value = 2024, message = "Publication year cannot be in the future.")
        Integer publicationYear,
        @IsbnFormat
        @IsbnUniqueness
        String isbn,
        String genre,
        @Min(value = 0, message = "Copies available should not be less than 0.")
        Integer copiesAvailable
) {
}
