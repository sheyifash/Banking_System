package com.example.bankingsystem.Dto;

import lombok.Data;

@Data
public class ResetPasswordReq {
    private String newPassword;
    private String confirmPassword;
}
