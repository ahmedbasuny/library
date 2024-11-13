package com.library.domain.book.exceptions;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(String message) {
        super(message);
    }

    public static BookNotFoundException forId(Long id) {
        return new BookNotFoundException("Book with id: " + id + " not found.");
    }
}
