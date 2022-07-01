package com.bookstore.controller.dto;

import com.bookstore.common.Messages;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.enums.BookGenre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BookDTO {

    @NotEmpty(message = Messages.NOT_EMPTY_ISBN)
    @Size(min = 10, max = 50, message = Messages.REQUIRED_ISBN_LENGTH)
    private String isbn;

    @NotEmpty(message = Messages.NOT_EMPTY_TITLE)
    @Size(min = 3, max = 50, message = Messages.REQUIRED_TITLE_LENGTH)
    private String title;

    @NotNull(message = Messages.ADD_BOOK_AUTHORS)
    @Size(min = 1, message = Messages.ADD_BOOK_AUTHORS)
    @JsonBackReference
    private List<Author> author;

    @NotNull(message = Messages.PUBLISHER_MANDATORY)
    private Publisher publisher;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    public Book toEntity() {
        return Book.builder()
                .isbn(isbn)
                .title(title)
                .author(author)
                .publisher(publisher)
                .genre(genre).build();
    }

    public List<Author> getAuthor() {
        return author;
    }

}
