package com.library.domain.book;

import com.library.domain.book.exceptions.BookNotAvailableForBorrowingException;
import com.library.domain.book.exceptions.BookNotFoundException;
import com.library.domain.book.exceptions.DuplicateIsbnException;
import com.library.domain.book.models.Book;
import com.library.domain.book.models.CreateBookDto;
import com.library.domain.book.models.UpdateBookDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Book> getBooksWithPagination(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookRepository.findAll(pageable).map(BookMapper::bookEntityToBook);
    }

    @Override
    @Cacheable(value = "books", key = "#id")
    @Transactional(readOnly = true)
    public Book getBook(Long id) {
        BookEntity bookEntity = getBookEntityById(id);
        return BookMapper.bookEntityToBook(bookEntity);
    }

    @Override
    @Transactional
    public Book addBook(@Valid CreateBookDto createBookDto) {
        BookEntity bookEntity = bookRepository.save(
                BookMapper.createBookDtoToBookEntity(createBookDto));
        return BookMapper.bookEntityToBook(bookEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    @Transactional
    @CachePut(value = "books", key = "#id")
    public Book updateBook(Long id, @Valid UpdateBookDto updateBookDto) {

        BookEntity bookEntity = getBookEntityById(id);

        checkDuplicatedIsbn(id, updateBookDto, bookEntity);

        bookEntity.setTitle(updateBookDto.title());
        bookEntity.setAuthor(updateBookDto.author());
        bookEntity.setPublicationYear(updateBookDto.publicationYear());
        bookEntity.setIsbn(updateBookDto.isbn());
        bookEntity.setGenre(updateBookDto.genre());
        bookEntity.setCopiesAvailable(updateBookDto.copiesAvailable());
        BookEntity updateBookEntity = bookRepository.save(bookEntity);
        return BookMapper.bookEntityToBook(updateBookEntity);
    }

    private void checkDuplicatedIsbn(Long id, UpdateBookDto updateBookDto, BookEntity bookEntity) {
        if (updateBookDto.isbn() != null
                && !updateBookDto.isbn().equals(bookEntity.getIsbn())
                && bookRepository.existsByIsbnAndIdNot(updateBookDto.isbn(), id)) {
            throw new DuplicateIsbnException("The provided ISBN: " +
                    updateBookDto.isbn() + " already exists for another book.");
        }

    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#id")
    public void deleteBook(Long id) {
        BookEntity bookEntity = getBookEntityById(id);
        bookRepository.delete(bookEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public BookEntity checkBookAvailabilityForBorrowing(Long bookId) {
        BookEntity bookEntity = getBookEntityById(bookId);
        if (bookEntity.getCopiesAvailable() > 0) {
            return bookEntity;
        } else {
            throw BookNotAvailableForBorrowingException.forId(bookId);
        }
    }

    @Override
    @Transactional
    public void saveBook(BookEntity bookEntity) {
        bookRepository.save(bookEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public void returnBook(Long bookId) {
        BookEntity bookEntity = getBookEntityById(bookId);
        bookEntity.setCopiesAvailable(bookEntity.getCopiesAvailable() + 1);
        bookRepository.save(bookEntity);
    }

    private BookEntity getBookEntityById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> BookNotFoundException.forId(id));
    }
}
