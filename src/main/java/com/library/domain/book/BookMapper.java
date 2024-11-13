package com.library.domain.book;

import com.library.domain.book.models.Book;
import com.library.domain.book.models.CreateBookDto;
import org.springframework.stereotype.Component;

@Component
public class BookMapper {

    public static Book bookEntityToBook(BookEntity bookEntity) {
        return new Book(
                bookEntity.getId(),
                bookEntity.getTitle(),
                bookEntity.getAuthor(),
                bookEntity.getPublicationYear(),
                bookEntity.getIsbn(),
                bookEntity.getGenre(),
                bookEntity.getCopiesAvailable()
        );
    }

    public static BookEntity bookToBookEntity(Book book) {
        return BookEntity.builder()
                .title(book.title())
                .author(book.author())
                .publicationYear(book.publicationYear())
                .isbn(book.isbn())
                .genre(book.genre())
                .copiesAvailable(book.copiesAvailable())
                .build();
    }

    public static BookEntity createBookDtoToBookEntity(CreateBookDto createBookDto) {
        return BookEntity.builder()
                .title(createBookDto.title())
                .author(createBookDto.author())
                .publicationYear(createBookDto.publicationYear())
                .isbn(createBookDto.isbn())
                .genre(createBookDto.genre())
                .copiesAvailable(createBookDto.copiesAvailable())
                .build();
    }
}
