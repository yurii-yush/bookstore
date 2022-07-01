package com.bookstore.repository.specification;

import com.bookstore.entity.Client;
import com.bookstore.entity.metamodels.Client_;
import com.bookstore.controller.request.ClientSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class ClientSpecifications {

    private static Specification<Client> filterById(Long id) {
        return (root, query, cb) -> cb.equal(root.get(Client_.ID), id);
    }

    private static Specification<Client> filterBySurname(String surname) {
        return (root, query, cb) -> cb.like(root.get(Client_.SURNAME), "%" + surname + "%");
    }

    private static Specification<Client> filterByPhoneNumber(String phoneNumber) {
        return (root, query, cb) -> cb.like(root.get(Client_.PHONE_NUMBER), "%" + phoneNumber + "%");
    }

    private static Specification<Client> filterByEmail(String email) {
        return (root, query, cb) -> cb.like(root.get(Client_.EMAIL), "%" + email + "%");
    }

    public static Specification<Client> generateQuery(ClientSearchRequest request) {
        Specification<Client> query = GenericSpecifications.alwaysTrue();

        if (request.getId() != null) {
            query = query.and(filterById(request.getId()));
        }
        if (request.getSurname() != null) {
            query = query.and(filterBySurname(request.getSurname()));
        }
        if (request.getPhoneNumber() != null) {
            query = query.and(filterByPhoneNumber(request.getPhoneNumber()));
        }
        if (request.getEmail() != null) {
            query = query.and(filterByEmail(request.getEmail()));
        }

        return query;
    }
}
