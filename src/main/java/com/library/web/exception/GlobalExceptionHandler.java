package com.library.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.library.domain.book.exceptions.BookNotAvailableForBorrowingException;
import com.library.domain.book.exceptions.BookNotFoundException;
import com.library.domain.book.exceptions.DuplicateIsbnException;
import com.library.domain.borrowing.exception.BorrowingRecordNotFoundException;
import com.library.domain.borrowing.exception.PatronAlreadyBorrowedBookException;
import com.library.domain.patron.exception.PatronNotAllowedToBorrowBooksException;
import com.library.domain.patron.exception.PatronNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final String TIMESTAMP_KEY = "timestamp";

    public static final DateTimeFormatter FORMATTER = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC"));


    @ExceptionHandler(Exception.class)
    ProblemDetail handle(Exception exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(BookNotFoundException.class)
    ProblemDetail handle(BookNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Book Not Found");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(BookNotAvailableForBorrowingException.class)
    ProblemDetail handle(BookNotAvailableForBorrowingException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("Book Not Available");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    ProblemDetail handle(DuplicateIsbnException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("ISBN Duplicated");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(PatronNotFoundException.class)
    ProblemDetail handle(PatronNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Patron Not Found");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(PatronNotAllowedToBorrowBooksException.class)
    ProblemDetail handle(PatronNotAllowedToBorrowBooksException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT, exception.getMessage());
        problemDetail.setTitle("Patron Not Allowed To Borrow");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(BorrowingRecordNotFoundException.class)
    ProblemDetail handle(BorrowingRecordNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND, exception.getMessage());
        problemDetail.setTitle("Borrowing Data Not Found");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(PatronAlreadyBorrowedBookException.class)
    ProblemDetail handle(PatronAlreadyBorrowedBookException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.FORBIDDEN, exception.getMessage());
        problemDetail.setTitle("Patron Already Borrowed The Book");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    ProblemDetail handle(UsernameNotFoundException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setTitle("User Not Found");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(AuthenticationException.class)
    public ProblemDetail handle(AuthenticationException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setTitle("Authentication Failed");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ProblemDetail handle(ExpiredJwtException exception) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.UNAUTHORIZED, exception.getMessage());
        problemDetail.setTitle("Token Expired");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, String> fieldErrors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            fieldErrors.put(fieldName, message);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Validation Error");
        problemDetail.setProperty("fieldErrors", fieldErrors);
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }


    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException exception,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Bad Request");
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ProblemDetail> handle(InvalidFormatException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        exception.getPath().forEach(reference -> {
            String fieldName = reference.getFieldName();
            String message = reference.getDescription();
            fieldErrors.put(fieldName, message);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Validation Format Error");
        problemDetail.setProperty("fieldErrors", fieldErrors);
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return new ResponseEntity<>(problemDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handle(ConstraintViolationException exception) {
        Map<String, String> fieldErrors = new HashMap<>();
        Set<ConstraintViolation<?>> constraintViolations = exception.getConstraintViolations();
        constraintViolations.forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String message = violation.getMessage();
            fieldErrors.put(fieldName, message);
        });

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST, exception.getMessage());
        problemDetail.setTitle("Constraint Violation Error");
        problemDetail.setProperty("fieldErrors", fieldErrors);
        problemDetail.setProperty(TIMESTAMP_KEY, getCurrentTimeFormatted());
        return problemDetail;
    }

    private static String getCurrentTimeFormatted() {
        return FORMATTER.format(Instant.now());
    }

}
