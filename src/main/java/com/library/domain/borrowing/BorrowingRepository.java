package com.library.domain.borrowing;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface BorrowingRepository extends JpaRepository<BorrowingRecordEntity, Long> {
    Optional<BorrowingRecordEntity> findByBookIdAndPatronId(Long bookId, Long patronId);
}
