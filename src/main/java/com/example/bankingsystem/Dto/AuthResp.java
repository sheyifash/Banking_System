package com.example.bankingsystem.Dto;

import com.example.bankingsystem.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class AuthResp {
    private String message;
    private Status status;
    private String token;
    private String accountNumber;
    private long accountBalance;
    private LocalDateTime createdAt;

    public AuthResp(String token) {
    }
}
