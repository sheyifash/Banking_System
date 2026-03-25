package com.example.bankingsystem.Dto;

import lombok.Data;

@Data
public class TransferReq {
    private long amount;
    private String accountNumber;
    private String narration;
    private String idempotenyKey;

    public String getIdempotencyKey() {
        return idempotenyKey;
    }
}
