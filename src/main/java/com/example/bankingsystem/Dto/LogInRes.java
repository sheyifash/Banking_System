package com.example.bankingsystem.Dto;

import com.example.bankingsystem.Enum.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
public class LogInRes {
    private String message;
    private Status status;
    private String token;
    private LocalDateTime loggedInAt;

    public LogInRes(String token) {
    }

}
