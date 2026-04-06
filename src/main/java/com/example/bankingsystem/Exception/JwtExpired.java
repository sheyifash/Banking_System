package com.example.bankingsystem.Exception;

public class JwtExpired extends RuntimeException {
    public JwtExpired(String message) {
        super(message);
    }
    public JwtExpired(String message, Throwable cause) {
        super(message, cause);
    }
}
