package com.library.domain.book;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    @Override
    public Page<Book> getBooksWithPagination(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return bookRepository.findAll(pageable).map(BookMapper::bookEntityToBook);
    }

    @Override
    public Optional<Book> getBook(Long id) {
        return bookRepository.findById(id).map(BookMapper::bookEntityToBook);
    }

    @Override
    @Transactional
    public Book addBook(Book book) {
        BookEntity bookEntity = bookRepository.save(BookMapper.bookToBookEntity(book));
        return BookMapper.bookEntityToBook(bookEntity);
    }

    @Override
    public boolean existsByIsbn(String isbn) {
        return bookRepository.existsByIsbn(isbn);
    }

    @Override
    @Transactional
    @Modifying
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
    @Modifying
    public void deleteBook(Long id) {
        BookEntity bookEntity = getBookEntityById(id);
        bookRepository.delete(bookEntity);
    }

    private BookEntity getBookEntityById(Long id) {
        return bookRepository.findById(id).orElseThrow(
                () -> BookNotFoundException.forId(id));
    }
}
