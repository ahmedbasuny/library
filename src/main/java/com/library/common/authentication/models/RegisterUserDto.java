package com.library.common.authentication.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public record RegisterUserDto(
        @Email(message = "Email should be in valid format.")
        String email,
        @NotEmpty(message = "First Name is required.")
        String firstName,
        @NotEmpty(message = "Last Name is required.")
        String lastName,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = """
                        Password must be at least 8 characters long,
                        and include at least one uppercase letter,
                        one lowercase letter, one digit, and one special character.
                        """)
        String password
) {
}
