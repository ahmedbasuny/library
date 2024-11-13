package com.library.domain.user;

public record User(
        Long id,
        String email,
        String firstName,
        String lastName
) {
}
