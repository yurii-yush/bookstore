package com.bookstore.repository.specification;

import com.bookstore.entity.Book;
import com.bookstore.entity.enums.BookGenre;
import com.bookstore.entity.metamodels.Book_;
import com.bookstore.controller.request.BookSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class BookSpecifications {

    private static Specification<Book> filterByIsbn(String isbn) {
        return (root, query, cb) -> cb.equal(root.get(Book_.ISBN), isbn);
    }

    private static Specification<Book> filterByTitle(String title) {
        return (root, query, cb) -> cb.like(root.get(Book_.TITLE), "%" + title + "%");
    }

    private static Specification<Book> filterByAuthor(Long authorId) {
        return (root, query, cb) -> cb.equal(root.join(Book_.AUTHOR), authorId);
    }

    private static Specification<Book> filterByPublisherId(Long publisherId) {
        return (root, query, cb) -> cb.equal(root.get(Book_.PUBLISHER), publisherId);
    }

    private static Specification<Book> filterByGenre(BookGenre genre) {
        return (root, query, cb) -> cb.equal(root.get(Book_.GENRE), genre);
    }

    public static Specification<Book> generateQuery(BookSearchRequest request) {
        Specification<Book> query = GenericSpecifications.alwaysTrue();

        if (request.getIsbn() != null) {
            query = query.and(filterByIsbn(request.getIsbn()));
        }
        if (request.getTitle() != null) {
            query = query.and(filterByTitle(request.getTitle()));
        }
        if (request.getGenre() != null) {
            query = query.and(filterByGenre(request.getGenre()));
        }
        if (request.getAuthorId() != null) {
            query = query.and(filterByAuthor(request.getAuthorId()));
        }
        if (request.getPublisherId() != null) {
            query = query.and(filterByPublisherId(request.getPublisherId()));
        }
        return query;
    }

}
