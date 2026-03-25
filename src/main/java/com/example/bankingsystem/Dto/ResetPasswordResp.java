package com.example.bankingsystem.Dto;

import com.example.bankingsystem.Enum.Status;
import lombok.Data;

@Data
public class ResetPasswordResp {
    private String message;
    private Status status;
}
