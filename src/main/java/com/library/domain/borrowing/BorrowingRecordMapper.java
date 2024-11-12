package com.library.domain.borrowing;

import com.library.domain.book.Book;
import com.library.domain.book.BookMapper;
import com.library.domain.patron.Patron;
import com.library.domain.patron.PatronMapper;
import org.springframework.stereotype.Component;

@Component
public class BorrowingRecordMapper {
    public static BorrowingRecord borrowingRecordEntityToBorrowingRecord(
            BorrowingRecordEntity borrowingRecordEntity) {
        Book book = BookMapper.bookEntityToBook(borrowingRecordEntity.getBook());
        Patron patron = PatronMapper.patronEntityToPatron(borrowingRecordEntity.getPatron());
        return new BorrowingRecord(
                borrowingRecordEntity.getId(),
                book,
                patron,
                borrowingRecordEntity.getBorrowDate(),
                borrowingRecordEntity.getReturnDate(),
                borrowingRecordEntity.getDueDate()
        );
    }
}
