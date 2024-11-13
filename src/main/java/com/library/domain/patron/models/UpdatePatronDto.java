package com.library.domain.patron.models;

import com.library.common.enums.PatronStatus;
import com.library.common.validation.enums.EnumValidation;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDate;

public record UpdatePatronDto(
        @NotEmpty(message = "Name is required.")
        String name,
        @NotEmpty(message = "Mobile is required.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid mobile number.")
        String mobile,
        @Email(message = "Invalid email format.")
        String email,
        String address,
        @PastOrPresent(message = "Membership date cannot be in the future.")
        LocalDate membershipDate,
        @EnumValidation(PatronStatus.class)
        PatronStatus status
) {
}