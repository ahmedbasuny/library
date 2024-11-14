package com.library.common.authentication.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

public record AccessTokenRequestDto(
        @Email(message = "Email should be in valid format.")
        String email,
        @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = """
                        Password must be at least 8 characters long,
                        and include at least one uppercase letter,
                        one lowercase letter, one digit, and one special character.
                        """)
        String password
) {
}
