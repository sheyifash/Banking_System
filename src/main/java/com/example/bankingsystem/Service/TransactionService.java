package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.TransferReq;
import com.example.bankingsystem.Dto.TransferResp;
import com.example.bankingsystem.Entity.TransactionModel;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TransactionService {

    public TransferResp Transfer(TransferReq req, Authentication auth);
    public TransferResp ReceiveMoney(String account, long amount);
    public List<TransactionModel> getTransactionHistory(String accountNumber);
}
