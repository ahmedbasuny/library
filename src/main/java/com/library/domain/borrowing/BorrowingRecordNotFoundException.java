package com.library.domain.borrowing;

public class BorrowingRecordNotFoundException extends RuntimeException {

    public BorrowingRecordNotFoundException(Long bookId, Long patronId) {
        super("Borrowing Record not found with book id: " + bookId + " and patron id: " + patronId);
    }

}
