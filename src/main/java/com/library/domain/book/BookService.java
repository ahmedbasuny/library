package com.library.domain.book;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface BookService {
    Page<Book> getBooksWithPagination(int pageNumber, int pageSize);

    Optional<Book> getBook(Long id);

    Book addBook(Book book);

    boolean existsByIsbn(String isbn);

    Book updateBook(Long id, Book book);

    void deleteBook(Long id);
}
