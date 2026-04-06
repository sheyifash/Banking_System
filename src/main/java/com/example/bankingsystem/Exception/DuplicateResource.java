package com.example.bankingsystem.Exception;

public class DuplicateResource extends RuntimeException {
    public DuplicateResource(String message) {
        super(message);
    }
    public DuplicateResource(String message, Throwable cause){
        super(message, cause);
    }
}
