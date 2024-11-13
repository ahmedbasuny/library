package com.library.domain.book.exceptions;

public class BookNotAvailableForBorrowingException extends RuntimeException {

    public BookNotAvailableForBorrowingException(String message) {
        super(message);
    }

    public static BookNotAvailableForBorrowingException forId(Long id) {
        return new BookNotAvailableForBorrowingException(
                "Book with id: " + id + " not available for borrowing for now.");
    }
}
