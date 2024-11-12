package com.library.domain.book;

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
}
