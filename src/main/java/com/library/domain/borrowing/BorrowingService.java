package com.library.domain.borrowing;

import org.springframework.stereotype.Service;

@Service
public interface BorrowingService {

    BorrowingRecord borrowBook(Long bookId, Long patronId);

    BorrowingRecord returnBook(Long bookId, Long patronId);
}
