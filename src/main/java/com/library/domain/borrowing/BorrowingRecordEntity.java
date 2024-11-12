package com.library.domain.borrowing;

import com.library.domain.book.BookEntity;
import com.library.domain.patron.PatronEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "borrowing_record")
public class BorrowingRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "borrowing_record_seq_gen")
    @SequenceGenerator(name = "borrowing_record_seq_gen", sequenceName = "borrowing_record_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "book_id", nullable = false)
    @NotNull(message = "Book is required.")
    private BookEntity book;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patron_id", nullable = false)
    @NotNull(message = "Patron is required.")
    private PatronEntity patron;

    @Column(nullable = false)
    @NotNull(message = "Borrow date is required.")
    @PastOrPresent(message = "Borrow date cannot be in the future.")
    private LocalDate borrowDate;

    @FutureOrPresent(message = "Return date cannot be in the past.")
    private LocalDate returnDate;

    @FutureOrPresent(message = "Due date cannot be in the past.")
    private LocalDate dueDate;

}
