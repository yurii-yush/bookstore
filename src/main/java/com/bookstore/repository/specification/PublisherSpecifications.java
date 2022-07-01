package com.bookstore.repository.specification;

import com.bookstore.entity.Publisher;
import com.bookstore.entity.metamodels.Publisher_;
import com.bookstore.controller.request.PublisherSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class PublisherSpecifications {

    private static Specification<Publisher> filterById(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Publisher_.ID), id);
    }

    private static Specification<Publisher> filterByTitle(String title) {
        return (root, query, cb) -> cb.like(root.get(Publisher_.TITLE), "%" + title + "%");
    }

    private static Specification<Publisher> filterByCountry(String country) {
        return (root, query, cb) -> cb.equal(root.get(Publisher_.COUNTRY), country);
    }

    public static Specification<Publisher> generateQuery(PublisherSearchRequest request) {

        Specification<Publisher> query = GenericSpecifications.alwaysTrue();

        if (request.getId() != null) {
            query = query.and(filterById(request.getId()));
        }
        if (request.getTitle() != null) {
            query = query.and(filterByTitle(request.getTitle()));
        }
        if (request.getCountry() != null) {
            query = query.and(filterByCountry(request.getCountry()));
        }

        return query;
    }
}
