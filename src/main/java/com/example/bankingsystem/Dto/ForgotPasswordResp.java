package com.example.bankingsystem.Dto;

import com.example.bankingsystem.Enum.Status;
import lombok.Data;

@Data
public class ForgotPasswordResp {
    private String message;
    private Status status;

    public ForgotPasswordResp(String resetToken) {
    }
}
