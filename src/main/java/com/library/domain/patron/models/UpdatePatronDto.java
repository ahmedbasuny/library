package com.library.domain.patron.models;

import com.library.common.enums.PatronStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record UpdatePatronDto(
        @NotEmpty(message = "Name is required.")
        String name,
        @NotEmpty(message = "Mobile is required.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid mobile number.")
        String mobile,
        @Email(message = "Email should be in valid format.")
        String email,
        String address,
        @PastOrPresent(message = "Membership date cannot be in the future.")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        LocalDate membershipDate,
        PatronStatus status
) {
}