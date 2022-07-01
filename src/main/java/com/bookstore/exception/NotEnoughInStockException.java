package com.bookstore.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
public class NotEnoughInStockException extends RuntimeException {

    public NotEnoughInStockException(String message) {
        super(message);
    }
}
