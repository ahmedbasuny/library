package com.library.domain.book;

import com.library.domain.book.models.Book;
import com.library.domain.book.models.CreateBookDto;
import com.library.domain.book.models.UpdateBookDto;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface BookService {
    Page<Book> getBooksWithPagination(int pageNumber, int pageSize);

    Book getBook(Long id);

    Book addBook(@Valid CreateBookDto createBookDto);

    boolean existsByIsbn(String isbn);

    Book updateBook(Long id, @Valid UpdateBookDto updateBookDto);

    void deleteBook(Long id);

    BookEntity checkBookAvailabilityForBorrowing(Long bookId);

    void saveBook(BookEntity bookEntity);

    void returnBook(Long bookId);
}
