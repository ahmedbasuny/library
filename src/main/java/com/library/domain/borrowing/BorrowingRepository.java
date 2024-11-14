package com.library.domain.borrowing;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface BorrowingRepository extends JpaRepository<BorrowingRecordEntity, Long> {
    Optional<BorrowingRecordEntity> findByBookIdAndPatronId(Long bookId, Long patronId);

    @Query(""" 
            SELECT b FROM BorrowingRecordEntity b
            WHERE b.book.id = :bookId
            AND b.patron.id = :patronId
            AND b.returnDate IS NULL
            """)
    List<BorrowingRecordEntity> findActiveBorrowingRecord(
            @Param("bookId") Long bookId,
            @Param("patronId") Long patronId
    );

}
