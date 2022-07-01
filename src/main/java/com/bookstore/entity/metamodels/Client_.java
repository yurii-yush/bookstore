package com.bookstore.entity.metamodels;


import com.bookstore.entity.Client;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Client.class)
public abstract class Client_ {

    public static volatile SingularAttribute<Client, Long> id;
    public static volatile SingularAttribute<Client, String> name;
    public static volatile SingularAttribute<Client, String> surname;
    public static volatile SingularAttribute<Client, String> phoneNumber;
    public static volatile SingularAttribute<Client, String> email;
    public static volatile SingularAttribute<Client, String> address;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String PHONE_NUMBER = "phoneNumber";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
}
