package com.library.domain.borrowing.exception;

public class PatronAlreadyBorrowedBookException extends RuntimeException {

    public PatronAlreadyBorrowedBookException(Long bookId, Long patronId) {
        super("Patron with id: " + patronId + " has borrowed book with id: " + bookId);
    }

}
