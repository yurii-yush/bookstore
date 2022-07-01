package com.bookstore.repository.specification;


import com.bookstore.entity.Order;
import com.bookstore.entity.enums.OrderStatus;
import com.bookstore.entity.metamodels.Order_;
import com.bookstore.controller.request.OrderSearchRequest;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public class OrderSpecifications {

    private static Specification<Order> filterByStatus(OrderStatus status) {
        return (root, query, cb) -> cb.equal(root.get(Order_.STATUS), status);
    }

    private static Specification<Order> filterByClientId(Long clientId) {
        return (root, query, cb) -> cb.equal(root.get(Order_.CLIENT), clientId);
    }

    private static Specification<Order> filterById(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Order_.ID), id);
    }

    private static Specification<Order> filterByDatetimeFrom(Instant datetimeFrom) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(Order_.DATETIME), datetimeFrom);
    }

    private static Specification<Order> filterByDatetimeTo(Instant datetimeTo) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(Order_.DATETIME), datetimeTo);
    }

    public static Specification<Order> generateQuery(OrderSearchRequest request) {

        Specification<Order> query = GenericSpecifications.alwaysTrue();

        if (request.getDatetimeFrom() != null) {
            query = query.and(filterByDatetimeFrom(request.getDatetimeFrom()));
        }
        if (request.getDatetimeFrom() != null) {
            query = query.and(filterById(request.getId()));
        }
        if (request.getDatetimeTo() != null) {
            query = query.and(filterByDatetimeTo(request.getDatetimeTo()));
        }
        if (request.getStatus() != null) {
            query = query.and(filterByStatus(request.getStatus()));
        }
        if (request.getClientId() != null) {
            query = query.and(filterByClientId(request.getClientId()));
        }
        if (request.getId() != null) {
            query = query.and(filterById(request.getId()));
        }

        return query;
    }
}
