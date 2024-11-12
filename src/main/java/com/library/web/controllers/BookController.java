package com.library.web.controllers;

import com.library.domain.book.Book;
import com.library.domain.book.BookNotFoundException;
import com.library.domain.book.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping
    ResponseEntity<Page<Book>> getBooksWithPagination(
            @RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(bookService.getBooksWithPagination(pageNumber, pageSize));
    }

    @PostMapping
    ResponseEntity<Book> addBook(@Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> getBook(@PathVariable Long id) {
        return bookService.getBook(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> BookNotFoundException.forId(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book book) {
        return new ResponseEntity<>(bookService.updateBook(id, book), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
