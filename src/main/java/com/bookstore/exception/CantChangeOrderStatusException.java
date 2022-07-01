package com.bookstore.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CantChangeOrderStatusException extends RuntimeException {
    public CantChangeOrderStatusException(String message) {
        super(message);
    }
}
