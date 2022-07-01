package com.bookstore.entity.metamodels;


import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.enums.BookGenre;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.List;

@StaticMetamodel(Book.class)
public abstract class Book_ {

    public static volatile SingularAttribute<Book, String> isbn;
    public static volatile SingularAttribute<Book, String> title;
    public static volatile SingularAttribute<Book, Publisher> publisher;
    public static volatile SingularAttribute<Book, List<Author>> author;
    public static volatile SingularAttribute<Book, BookGenre> genre;

    public static final String ISBN = "isbn";
    public static final String TITLE = "title";
    public static final String PUBLISHER = "publisher";
    public static final String AUTHOR = "author";
    public static final String GENRE = "genre";
}
