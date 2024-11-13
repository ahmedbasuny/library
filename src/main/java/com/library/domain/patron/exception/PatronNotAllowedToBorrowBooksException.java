package com.library.domain.patron.exception;

public class PatronNotAllowedToBorrowBooksException extends RuntimeException {

    public PatronNotAllowedToBorrowBooksException(String message) {
        super(message);
    }

    public static PatronNotAllowedToBorrowBooksException forId(Long id) {
        return new PatronNotAllowedToBorrowBooksException(
                "Patron with id: " + id + " not allowed to borrow books.");
    }
}
