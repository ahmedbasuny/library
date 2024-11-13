package com.library.domain.borrowing;

import com.library.domain.book.models.Book;
import com.library.domain.patron.models.Patron;

import java.time.LocalDate;

public record BorrowingRecord(
        Long id,
        Book book,
        Patron patron,
        LocalDate borrowDate,
        LocalDate returnDate,
        LocalDate dueDate
) {
}