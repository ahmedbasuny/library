package com.library.domain.patron.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.library.common.enums.PatronStatus;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Patron.class, name = "Patron") // Add more subtypes here if needed
})
public record Patron(
        Long id,
        @NotEmpty(message = "Name is required.")
        String name,
        @NotEmpty(message = "Mobile is required.")
        @Pattern(regexp = "^\\+?[0-9. ()-]{7,25}$", message = "Invalid mobile number.")
        String mobile,
        @Email(message = "Invalid email format.")
        String email,
        String address,
        @PastOrPresent(message = "Membership date cannot be in the future.")
        @JsonDeserialize(using = LocalDateDeserializer.class)
        @JsonSerialize(using = LocalDateSerializer.class)
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        LocalDate membershipDate,
        PatronStatus status
) {
}