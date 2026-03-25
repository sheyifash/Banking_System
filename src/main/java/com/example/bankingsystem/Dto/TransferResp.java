package com.example.bankingsystem.Dto;

import com.example.bankingsystem.Enum.Status;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TransferResp {
    private Status status;
    private String message;
    private long amount;
    private String senderName;
    private String receiverName;
    private long accountBalance;
    private LocalDateTime transactionTime;
    private String transactionId;
}
