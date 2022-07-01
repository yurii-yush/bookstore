package com.bookstore.common;

public interface Messages {

    String NOT_EMPTY_NAME = "Name shouldn't be empty.";
    String REQUIRED_NAME_LENGTH = "Name length should be 3-30 letters.";
    String NOT_EMPTY_SURNAME = "Surname shouldn't be empty.";
    String REQUIRED_SURNAME_LENGTH = "Surname length should be 3-40 letters.";
    String NOT_EMPTY_ISBN = "ISBN shouldn't be empty.";
    String REQUIRED_ISBN_LENGTH = "ISBN length should be 10-50 letters.";
    String NOT_EMPTY_TITLE = "Title shouldn't be empty.";
    String REQUIRED_TITLE_LENGTH = "ISBN length should be 10-50 letters.";
    String ADD_BOOK_AUTHORS = "Add one or more authors.";
    String PUBLISHER_MANDATORY = "Publisher is mandatory.";
    String PHONE_NUMBER_PATTERN_MESSAGE = "Pattern: +38(067)-55-55-555";
    String NOT_EMPTY_ADDRESS = "Address shouldn't be empty.";
    String NOT_EMPTY_EMAIL = "Email shouldn't be empty.";
    String NOT_VALID_EMAIL = "Email not valid.";
    String REQUIRED_ADDRESS_LENGTH = "Address length should be 15-50 letters.";
    String ADD_BOOKS_TO_ORDER = "Add to order one or more books.";
    String ADD_CLIENT_TO_ORDER = "Add client, please.";
    String BOOK_MANDATORY = "Book is mandatory.";
    String MIN_QUANTITY = "Quantity can't be less 1.";
    String AUTHOR_ID_NOT_FOUND = "Author by id: %s, not found.";
    String PUBLISHER_ID_NOT_FOUND = "Publisher by id: %s, not found.";
    String CLIENT_ID_NOT_FOUND = "Client by id: %s, not found.";
    String CLIENT_ALREADY_EXISTS = "Client with email %s already exists";
    String BOOK_ISBN_NOT_FOUND = "Book with isbn: %s, not found.";
    String MIN_PRICE = "Price can't be less 1UAH";
    String BOOK_IN_WAREHOUSE_NOT_FOUND = "There any book by isbn:  %s in warehouse.";
    String QUANTITY_BOOKS_IN_WAREHOUSE_NOT_ENOUGH = "Books with isbn %s  aren't enough in warehouse.";
    String ORDER_ID_NOT_FOUND = "Order by id: %s not found.";
    String ORDER_UPDATE_EXCEPTION = "Can't update because order is %s";
    String STATUS = "status";
    String CANT_CHANGE_ORDER_STATUS = "Can't change order status";
    String PHONE_NUMBER_REGEXP = "^\\+\\d{2}\\(\\d{3}\\)-\\d{2}-\\d{2}-\\d{3}";

    //Controllers URI
    String AUTHOR_CONTROLLER_URI = "/api/v1/authors";
    String CREATED_AUTHOR_URI = AUTHOR_CONTROLLER_URI + "/id=%s";
    String BOOK_CONTROLLER_URI = "/api/v1/books";
    String CREATED_BOOK_URI = BOOK_CONTROLLER_URI + "/isbn=%s";
    String CLIENT_CONTROLLER_URI = "/api/v1/clients";
    String CREATED_CLIENT_URI = CLIENT_CONTROLLER_URI + "/id=%s";
    String ORDER_CONTROLLER_URI = "/api/v1/orders";
    String CREATED_ORDER_URI = ORDER_CONTROLLER_URI + "/id=%s";
    String PUBLISHER_CONTROLLER_URI = "/api/v1/publishers";
    String CREATED_PUBLISHER_URI = PUBLISHER_CONTROLLER_URI + "/id=%s";
    String WAREHOUSE_CONTROLLER_URI = "/api/v1/warehouse";
    String CREATED_WAREHOUSE_URI = WAREHOUSE_CONTROLLER_URI + "/id=%s";
    String ID_PATH = "id";
    String ID_MAPPING = "/{id}";
    String ISBN_PATH = "isbn";
    String PRICE_PATH = "price";
    String QUANTITY_PATH = "quantity";
    String ISBN_MAPPING = "/{isbn}";

    //Exceptions messages
    String ENTITY_NOT_FOUND = "Entity not found";
    String WRONG_BOOK_QUANTITY = "Wrong book quantity";
    String VALIDATION_FAILED = "Validation Failed";
    String FIELD_VALIDATION_FAILED = "Field validation failed";
    String NOT_ENOUGH_IN_STOCK = "Not enough in stock";
    String CANT_CHANGE_STATUS = "Can't change order status";
    String FAILED_TO_CONVERT_VALUE = "Failed to convert value";
    String NULL_POINTER_EXCEPTION = "NullPointerException";
    String OTHER_EXCEPTION = "Something was wrong. Try later";
}
