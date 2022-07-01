package com.bookstore.entity.metamodels;

import com.bookstore.entity.Author;
import com.bookstore.entity.Book;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.util.List;

@StaticMetamodel(Author.class)
public abstract class Author_ {
    public static volatile SingularAttribute<Author, Long> id;
    public static volatile SingularAttribute<Author, String> name;
    public static volatile SingularAttribute<Author, String> surname;
    public static volatile SingularAttribute<Author, String> country;
    public static volatile SingularAttribute<Author, List<Book>> books;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String COUNTRY = "country";
    public static final String BOOKS = "books";
}
