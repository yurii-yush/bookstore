package com.bookstore.repository.specification;


import com.bookstore.entity.StockItem;
import com.bookstore.entity.metamodels.StockItem_;
import com.bookstore.controller.request.WarehouseSearchRequest;
import org.springframework.data.jpa.domain.Specification;

public class WarehouseSpecifications {
    private static Specification<StockItem> filterByBookIsbn(String bookIsbn) {
        return (root, query, cb) -> cb.equal(root.get(StockItem_.BOOK_ISBN), bookIsbn);
    }

    private static Specification<StockItem> filterByPriceFrom(double priceFrom) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(StockItem_.PRICE), priceFrom);
    }

    private static Specification<StockItem> filterByPriceTo(double priceTo) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get(StockItem_.PRICE), priceTo);
    }

    private static Specification<StockItem> filterByQuantity(int quantity) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get(StockItem_.QUANTITY), quantity);
    }

    public static Specification<StockItem> generateQuery(WarehouseSearchRequest request) {

        Specification<StockItem> query = GenericSpecifications.alwaysTrue();

        if (request.getBookIsbn() != null) {
            query = query.and(filterByBookIsbn(request.getBookIsbn()));
        }
        if (request.getPriceFrom() != null) {
            query = query.and(filterByPriceFrom(request.getPriceFrom()));
        }
        if (request.getPriceTo() != null) {
            query = query.and(filterByPriceTo(request.getPriceTo()));
        }
        if (request.getQuantity() != null) {
            query = query.and(filterByQuantity(request.getQuantity()));
        }

        return query;
    }
}
