package com.example.bankingsystem.Exception;

public class InvalidAmount extends RuntimeException {
    public InvalidAmount(String message) {
        super(message);
    }
    public InvalidAmount(String message, Throwable cause) {
        super(message, cause);
    }
}
