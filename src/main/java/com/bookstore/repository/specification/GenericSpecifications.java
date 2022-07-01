package com.bookstore.repository.specification;

import org.springframework.data.jpa.domain.Specification;

public class GenericSpecifications {
    public static <T> Specification<T> alwaysTrue() {
        return Specification.where((root, query, cb) -> cb.equal(cb.literal(1), 1));
    }
}
