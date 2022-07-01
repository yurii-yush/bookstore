package com.bookstore.repository.specification;

import com.bookstore.entity.Author;
import com.bookstore.entity.metamodels.Author_;
import com.bookstore.controller.request.AuthorSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class AuthorSpecifications {

    private static Specification<Author> filterById(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Author_.ID), id);
    }

    private static Specification<Author> filterBySurname(String surname) {
        return (root, query, cb) -> cb.like(root.get(Author_.SURNAME), "%" + surname + "%");
    }

    private static Specification<Author> filterByCountry(String country) {
        return (root, query, cb) -> cb.equal(root.get(Author_.COUNTRY), country);
    }

    public static Specification<Author> generateQuery(AuthorSearchRequest request) {

        Specification<Author> query = GenericSpecifications.alwaysTrue();

        if (request.getId() != null) {
            query = query.and(filterById(request.getId()));
        }
        if (request.getSurname() != null) {
            query = query.and(filterBySurname(request.getSurname()));
        }
        if (request.getCountry() != null) {
            query = query.and(filterByCountry(request.getCountry()));
        }

        return query;
    }
}
