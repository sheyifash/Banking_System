package com.example.bankingsystem.Controller;

import com.example.bankingsystem.Dto.TransferReq;
import com.example.bankingsystem.Dto.TransferResp;
import com.example.bankingsystem.Entity.TransactionModel;
import com.example.bankingsystem.Service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResp> transfer(@RequestBody TransferReq req) {
        return ResponseEntity.ok(transactionService.Transfer(req));
    }

    @PostMapping("/receive")
    public ResponseEntity<TransferResp> receiveMoney(
            @RequestParam String accountNumber,
            @RequestParam long amount
    ) {
        return ResponseEntity.ok(transactionService.ReceiveMoney(accountNumber, amount));
    }

    @GetMapping("/history/{accountNumber}")
    public ResponseEntity<List<TransactionModel>> getHistory(@PathVariable String accountNumber) {
        return ResponseEntity.ok(transactionService.getTransactionHistory(accountNumber));
    }
}