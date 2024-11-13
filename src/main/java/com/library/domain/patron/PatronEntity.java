package com.library.domain.patron;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.library.domain.borrowing.BorrowingRecordEntity;
import com.library.common.enums.PatronStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patron")
@ToString(exclude = {"borrowingRecords"})
public class PatronEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patron_seq_gen")
    @SequenceGenerator(name = "patron_seq_gen", sequenceName = "patron_id_seq", allocationSize = 1)
    private Long id;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private LocalDate membershipDate = LocalDate.now();
    private String status = PatronStatus.ACTIVE.name();

    @OneToMany(mappedBy = "patron", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<BorrowingRecordEntity> borrowingRecords;
}