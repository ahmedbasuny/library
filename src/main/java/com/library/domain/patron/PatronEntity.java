package com.library.domain.patron;

import com.library.domain.borrowing.BorrowingRecordEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "patron")
public class PatronEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patron_seq_gen")
    @SequenceGenerator(name = "patron_seq_gen", sequenceName = "patron_id_seq", allocationSize = 1)
    private Long id;

    @Column(nullable = false)
    @NotEmpty(message = "Name is required.")
    private String name;

    @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid mobile number.")
    private String mobile;

    @Email(message = "Invalid email format.")
    private String email;

    private String address;

    @Column(nullable = false)
    @PastOrPresent(message = "Membership date cannot be in the future.")
    private LocalDate membershipDate = LocalDate.now();

    @Column(nullable = false)
    @NotEmpty(message = "Status is required.")
    private String status = "active";

    @OneToMany(mappedBy = "patron", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BorrowingRecordEntity> borrowingRecords;
}