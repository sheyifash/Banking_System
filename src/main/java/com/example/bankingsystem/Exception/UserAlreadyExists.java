package com.example.bankingsystem.Exception;

public class UserAlreadyExists extends RuntimeException {
    public UserAlreadyExists(String message) {
        super(message);
    }
    public UserAlreadyExists(String message, Throwable cause) {
        super(message, cause);
    }
}
