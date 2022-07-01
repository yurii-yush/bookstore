package com.bookstore.exception;


import com.bookstore.common.Messages;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public final ResponseEntity<Object> handleEntityNotFoundException(Exception ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.ENTITY_NOT_FOUND, ex.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(error);
    }

    @ExceptionHandler(StockStoreException.class)
    public final ResponseEntity<Object> handleStockStoreException(Exception ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.WRONG_BOOK_QUANTITY, ex.getLocalizedMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ErrorAPI error = new ErrorAPI(Messages.VALIDATION_FAILED, details.toString());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.FIELD_VALIDATION_FAILED, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(NotEnoughInStockException.class)
    public final ResponseEntity<Object> handleNotEnoughInStockException(NotEnoughInStockException ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.NOT_ENOUGH_IN_STOCK, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(error);
    }

    @ExceptionHandler(CantChangeOrderStatusException.class)
    public final ResponseEntity<Object> handleCantChangeOrderStatusException(CantChangeOrderStatusException ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.CANT_CHANGE_STATUS, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public final ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.FAILED_TO_CONVERT_VALUE, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(error);
    }

    @ExceptionHandler(NullPointerException.class)
    public final ResponseEntity<Object> handleNullPointerException(Exception ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.NULL_POINTER_EXCEPTION, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllOtherExceptions(Exception ex, WebRequest request) {
        ErrorAPI error = new ErrorAPI(Messages.OTHER_EXCEPTION, ex.getLocalizedMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}
