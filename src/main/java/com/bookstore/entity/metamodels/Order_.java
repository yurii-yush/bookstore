package com.bookstore.entity.metamodels;


import com.bookstore.entity.Client;
import com.bookstore.entity.Order;
import com.bookstore.entity.SoldItem;
import com.bookstore.entity.enums.OrderStatus;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.Instant;
import java.util.List;

@StaticMetamodel(Order.class)
public abstract class Order_ {

    public static volatile SingularAttribute<Order, Long> id;
    public static volatile SingularAttribute<Order, List<SoldItem>> books;
    public static volatile SingularAttribute<Order, Client> client;
    public static volatile SingularAttribute<Order, OrderStatus> status;
    public static volatile SingularAttribute<Order, Instant> datetime;

    public static final String ID = "id";
    public static final String BOOKS = "books";
    public static final String CLIENT = "client";
    public static final String STATUS = "status";
    public static final String DATETIME = "datetime";
}
