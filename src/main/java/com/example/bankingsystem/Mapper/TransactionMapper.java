package com.example.bankingsystem.Mapper;

import com.example.bankingsystem.Dto.TransferReq;
import com.example.bankingsystem.Dto.TransferResp;
import com.example.bankingsystem.Entity.TransactionModel;
import com.example.bankingsystem.Repo.TransactionRepo;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public static TransferResp mapToDto(TransactionModel transactionModel){
        return TransferResp.builder()
                .transactionId(transactionModel.getTransactionId())
                .senderName(transactionModel.getSenderName())
                .receiverName(transactionModel.getReceiverName())
                .amount(transactionModel.getAmount())
                .accountBalance(transactionModel.getAccountBalance())
                .status(transactionModel.getStatus())
                .transactionTime(transactionModel.getTransactionTime())
                .message(transactionModel.getMessage())
                .build();
    }
    public static TransactionModel mapToEntity(TransferReq req){
        TransactionModel transactionModel = new TransactionModel();
                transactionModel.setAccountNumber(req.getAccountNumber());
                transactionModel.setAmount(req.getAmount());
                transactionModel.setNarration(req.getNarration());
                return transactionModel;
    }
}
