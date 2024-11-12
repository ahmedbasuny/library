package com.library.domain.borrowing;

import com.library.domain.book.BookEntity;
import com.library.domain.book.BookService;
import com.library.domain.patron.PatronEntity;
import com.library.domain.patron.PatronService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
class BorrowingServiceImpl implements BorrowingService {

    public static final int DAYS_TO_BORROW = 14;

    private final BookService bookService;
    private final PatronService patronService;
    private final BorrowingRepository borrowingRepository;

    @Override
    @Transactional
    public Object borrowBook(Long bookId, Long patronId) {
        BookEntity bookEntity = bookService.checkBookAvailabilityForBorrowing(bookId);
        PatronEntity patronEntity = patronService.checkPatronIsAllowedToBorrow(patronId);

        BorrowingRecordEntity borrowingRecordEntity = BorrowingRecordEntity.builder()
                .book(bookEntity)
                .patron(patronEntity)
                .borrowDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(DAYS_TO_BORROW))
                .build();
        BorrowingRecordEntity savedBorrowingRecord = borrowingRepository.save(borrowingRecordEntity);

        bookEntity.setCopiesAvailable(bookEntity.getCopiesAvailable() - 1);
        bookService.saveBook(bookEntity);

        return BorrowingRecordMapper
                .borrowingRecordEntityToBorrowingRecord(savedBorrowingRecord);
    }

    @Override
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        BorrowingRecordEntity borrowingRecordEntity = borrowingRepository
                .findByBookIdAndPatronId(bookId, patronId).orElseThrow(
                        () -> new BorrowingRecordNotFoundException(bookId, patronId));
        borrowingRecordEntity.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowingRecordEntity);

        bookService.returnBook(bookId);

        return BorrowingRecordMapper
                .borrowingRecordEntityToBorrowingRecord(borrowingRecordEntity);
    }
}
