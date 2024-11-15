package com.library.web.controllers;

import com.library.domain.book.models.Book;
import com.library.domain.book.BookService;
import com.library.domain.book.models.CreateBookDto;
import com.library.domain.book.models.UpdateBookDto;
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
    ResponseEntity<Book> addBook(@Valid @RequestBody CreateBookDto createBookDto) {
        return new ResponseEntity<>(bookService.addBook(createBookDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBook(id));
    }

    @PutMapping("/{id}")
    ResponseEntity<Book> updateBook(@PathVariable Long id,
                                    @Valid @RequestBody UpdateBookDto updateBookDto) {
        return new ResponseEntity<>(bookService.updateBook(id, updateBookDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Book> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
}
