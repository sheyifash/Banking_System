package com.example.bankingsystem.Exception;

public class InsufficientBalance extends RuntimeException {
    public InsufficientBalance(String message) {
        super(message);
    }
    public InsufficientBalance(String message, Throwable cause){
        super(message, cause);
    }
}
