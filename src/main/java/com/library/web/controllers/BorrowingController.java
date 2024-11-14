package com.library.web.controllers;


import com.library.domain.borrowing.BorrowingRecord;
import com.library.domain.borrowing.BorrowingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/borrow")
public class BorrowingController {

    private final BorrowingService borrowingService;

    @PostMapping("/{bookId}/patron/{patronId}")
    public ResponseEntity<BorrowingRecord> borrowBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId
    ) {
        return ResponseEntity.ok(borrowingService.borrowBook(bookId, patronId));
    }

    @PutMapping("/{bookId}/return/{patronId}")
    public ResponseEntity<BorrowingRecord> returnBook(
            @PathVariable Long bookId,
            @PathVariable Long patronId
    ) {
        return ResponseEntity.ok(borrowingService.returnBook(bookId, patronId));
    }

}
