package com.example.supermarket.exception;

public class CashierNotFoundException extends RuntimeException {

    public CashierNotFoundException(String message) {
        super(message);
    }
}
