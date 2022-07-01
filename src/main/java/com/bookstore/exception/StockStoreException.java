package com.bookstore.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class StockStoreException extends RuntimeException {

    public StockStoreException(String message) {
        super(message);
    }
}
