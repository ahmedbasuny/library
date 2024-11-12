package com.library.domain.book;

import com.library.domain.borrowing.BorrowingRecordEntity;
import com.library.domain.validation.isbn.IsbnFormat;
import com.library.domain.validation.isbn.IsbnUniqueness;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq_gen")
    @SequenceGenerator(name = "book_seq_gen", sequenceName = "book_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Title is required.")
    private String title;

    @Column(nullable = false)
    @NotEmpty(message = "Author is required.")
    private String author;

    @Max(value = 2024, message = "Publication year cannot be in the future.")
    private Integer publicationYear;

    @Column(unique = true, nullable = false, length = 13)
    private String isbn;

    private String genre;

    @Column(nullable = false)
    @Min(value = 0, message = "Copies available should not be less than 0.")
    private Integer copiesAvailable;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowingRecordEntity> borrowingRecords;
}
