package com.bookstore.entity.metamodels;

import com.bookstore.entity.StockItem;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(StockItem.class)
public abstract class StockItem_ {

    public static volatile SingularAttribute<StockItem, Long> id;
    public static volatile SingularAttribute<StockItem, String> bookIsbn;
    public static volatile SingularAttribute<StockItem, Double> price;
    public static volatile SingularAttribute<StockItem, Integer> quantity;

    public static final String ID = "id";
    public static final String BOOK_ISBN = "bookIsbn";
    public static final String PRICE = "price";
    public static final String QUANTITY = "quantity";

}
