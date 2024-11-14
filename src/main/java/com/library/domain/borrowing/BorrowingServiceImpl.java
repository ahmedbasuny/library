package com.library.domain.borrowing;

import com.library.domain.book.BookEntity;
import com.library.domain.book.BookService;
import com.library.domain.borrowing.exception.BorrowingRecordNotFoundException;
import com.library.domain.borrowing.exception.PatronAlreadyBorrowedBookException;
import com.library.domain.patron.PatronEntity;
import com.library.domain.patron.PatronService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
class BorrowingServiceImpl implements BorrowingService {

    public static final int DAYS_TO_BORROW = 14;

    private final BookService bookService;
    private final PatronService patronService;
    private final BorrowingRepository borrowingRepository;

    @Override
    @Transactional
    public BorrowingRecord borrowBook(Long bookId, Long patronId) {
        BookEntity bookEntity = bookService.checkBookAvailabilityForBorrowing(bookId);
        PatronEntity patronEntity = patronService.checkPatronIsAllowedToBorrow(patronId);
        checkPatronAlreadyBorrowedBook(bookId, patronId);

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
    @Transactional
    public BorrowingRecord returnBook(Long bookId, Long patronId) {
        List<BorrowingRecordEntity> borrowingRecordEntityList = borrowingRepository
                .findActiveBorrowingRecord(bookId, patronId);
        if (borrowingRecordEntityList.isEmpty()) {
            throw new BorrowingRecordNotFoundException(bookId, patronId);
        }
        BorrowingRecordEntity borrowingRecordEntity = borrowingRecordEntityList.getFirst();
        borrowingRecordEntity.setReturnDate(LocalDate.now());
        borrowingRepository.save(borrowingRecordEntity);

        bookService.returnBook(bookId);

        return BorrowingRecordMapper
                .borrowingRecordEntityToBorrowingRecord(borrowingRecordEntity);
    }

    private void checkPatronAlreadyBorrowedBook(Long bookId, Long patronId) {
        List<BorrowingRecordEntity> optionalBorrowingRecordEntity =
                borrowingRepository.findActiveBorrowingRecord(bookId, patronId);
        if (!optionalBorrowingRecordEntity.isEmpty()) {
            throw new PatronAlreadyBorrowedBookException(bookId, patronId);
        }
    }
}
