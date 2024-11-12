package com.library.domain.book;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public Book addBook(Book book) {
        BookEntity bookEntity = bookRepository.save(BookMapper.bookToBookEntity(book));
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
    public Book updateBook(Long id, Book book) {
        BookEntity bookEntity = getBookEntityById(id);
        bookEntity.setTitle(book.title());
        bookEntity.setAuthor(book.author());
        bookEntity.setPublicationYear(book.publicationYear());
        bookEntity.setIsbn(book.isbn());
        bookEntity.setGenre(book.genre());
        bookEntity.setCopiesAvailable(book.copiesAvailable());
        BookEntity updateBookEntity = bookRepository.save(bookEntity);
        return BookMapper.bookEntityToBook(updateBookEntity);
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
    public List<Book> getBooks() {
        return bookRepository.findAll().stream().map(
                BookMapper::bookEntityToBook).toList();
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
