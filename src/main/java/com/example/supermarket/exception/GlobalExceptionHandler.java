package com.example.supermarket.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        var error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),null);
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(CashierNotFoundException.class)
    public ResponseEntity<?> cashierNotFound(CashierNotFoundException ex) {
        var error = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(),null);
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException() {
        String errorMessage = "Provided value not found!";
        ErrorResponse error = new ErrorResponse(errorMessage,HttpStatus.NOT_FOUND.value(),null);
        return ResponseEntity.status(error.getStatusCode()).body(error);
    }
}
