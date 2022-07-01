package com.bookstore.entity.metamodels;

import com.bookstore.entity.Publisher;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Publisher.class)
public abstract class Publisher_ {

    public static volatile SingularAttribute<Publisher, Long> id;
    public static volatile SingularAttribute<Publisher, String> title;
    public static volatile SingularAttribute<Publisher, String> country;

    public static final String ID = "id";
    public static final String TITLE = "title";
    public static final String COUNTRY = "country";
}
