package com.bookstore.entity;

import com.bookstore.controller.dto.BookDTO;
import com.bookstore.entity.enums.BookGenre;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "book")
public class Book {

    @Id
    private String isbn;
    private String title;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "publisher_id", referencedColumnName = "id")
    private Publisher publisher;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "book_isbn"),
            inverseJoinColumns = @JoinColumn(name = "author_id"))
    @ToString.Exclude
    private List<Author> author;

    @Enumerated(EnumType.STRING)
    private BookGenre genre;

    public void addAuthor(Author author){
        this.author.add(author);
        author.getBooks().add(this);
    }
    public void removeAuthor(Author author){
        this.author.remove(author);
        author.getBooks().remove(this);
    }

    public BookDTO toDTO() {
        return BookDTO.builder()
                .isbn(isbn)
                .title(title)
                .publisher(publisher)
                .genre(genre)
                .author(author).build();
    }
}
