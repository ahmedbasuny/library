package com.library.domain.patron;

public class PatronNotFoundException extends RuntimeException {

    public PatronNotFoundException(String message) {
        super(message);
    }

    public static PatronNotFoundException forId(Long id) {
        return new PatronNotFoundException("Patron with id: " + id + " not found.");
    }
}
