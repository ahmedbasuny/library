package com.library.domain.book;

import com.library.domain.borrowing.BorrowingRecordEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
@ToString(exclude = {"borrowingRecords"})
public class BookEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq_gen")
    @SequenceGenerator(name = "book_seq_gen", sequenceName = "book_id_seq", allocationSize = 1)
    private Long id;
    private String title;
    private String author;
    private Integer publicationYear;
    private String isbn;
    private String genre;
    private Integer copiesAvailable;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowingRecordEntity> borrowingRecords;
}
