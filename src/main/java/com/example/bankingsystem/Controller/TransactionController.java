package com.example.bankingsystem.Controller;

import com.example.bankingsystem.Dto.TransferReq;
import com.example.bankingsystem.Dto.TransferResp;
import com.example.bankingsystem.Entity.TransactionModel;
import com.example.bankingsystem.Service.TransactionServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
private final TransactionServiceImpl transactionServiceImpl;

    public TransactionController(TransactionServiceImpl transactionServiceImpl) {
        this.transactionServiceImpl = transactionServiceImpl;
    }
    @PostMapping("/sendmoney")
    public TransferResp Transfer(@RequestBody TransferReq req){
    return transactionServiceImpl.Transfer(req);
    }

    @PostMapping("/addmoney")
    public TransferResp ReceiveMoney(@RequestBody String accountNumber, long amount){
        return transactionServiceImpl.ReceiveMoney(accountNumber, amount);
    }
    @GetMapping("/viewHistory")
    public List<TransactionModel> getTransactionHistory(@PathVariable String accountNumber){
        return transactionServiceImpl.getTransactionHistory(accountNumber);
    }
}

