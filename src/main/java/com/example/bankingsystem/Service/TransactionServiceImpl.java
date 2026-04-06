package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.TransferReq;
import com.example.bankingsystem.Dto.TransferResp;
import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Entity.TransactionModel;
import com.example.bankingsystem.Enum.Status;
import com.example.bankingsystem.Exception.InsufficientBalance;
import com.example.bankingsystem.Exception.InvalidAmount;
import com.example.bankingsystem.Exception.ResourceNotFound;
import com.example.bankingsystem.Mapper.TransactionMapper;
import com.example.bankingsystem.Repo.MerchantRepo;
import com.example.bankingsystem.Repo.TransactionRepo;
import org.jspecify.annotations.Nullable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TransactionServiceImpl implements TransactionService{
    private final MerchantRepo merchantRepo;
    private final TransactionRepo transactionRepo;

    public TransactionServiceImpl(MerchantRepo merchantRepo, TransactionRepo transactionRepo) {
        this.merchantRepo = merchantRepo;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public TransferResp Transfer(TransferReq req, Authentication auth) {

        //Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String senderEmail = auth.getName();

        MerchantModel sender = merchantRepo.findByEmail(senderEmail)
                .orElseThrow(() -> new ResourceNotFound("Sender not found"));

        MerchantModel receiver = merchantRepo.findByAccountNumber(req.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFound("Recipient does not exist"));
        if (sender.getAccountNumber().equals(receiver.getAccountNumber())) {
            System.out.println("You cannot transfer to yourself");
            throw new RuntimeException("You cannot transfer to yourself");
        }

        if (sender.getAccountBalance() < req.getAmount()) {
            System.out.println("Insufficient Balance");
            throw new InsufficientBalance("Insufficient Balance");
        }

        String idempotencyKey = req.getIdempotencyKey();

        Optional<TransactionModel> existingTxn =
                transactionRepo.findByIdempotencyKeyAndExpiresAtAfter(
                        idempotencyKey,
                        LocalDateTime.now()
                );

        if (existingTxn.isPresent()) {
            return TransactionMapper.mapToDto(existingTxn.get());
        }

        sender.setAccountBalance(sender.getAccountBalance() - req.getAmount());
        receiver.setAccountBalance(receiver.getAccountBalance() + req.getAmount());

        merchantRepo.save(sender);
        merchantRepo.save(receiver);

        TransactionModel transaction = new TransactionModel();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setIdempotencyKey(idempotencyKey);
        transaction.setAmount(req.getAmount());
        transaction.setSender(sender);
        transaction.setReceiver(receiver);
        transaction.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        transaction.setStatus(Status.SUCCESSFUL);

        transactionRepo.save(transaction);

        return TransactionMapper.mapToDto(transaction);
    }

    @Override
    public TransferResp ReceiveMoney(String accountNumber, long amount) {

        MerchantModel receiver = merchantRepo.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFound("Account not found"));

        if (amount <= 0) {
            throw new InvalidAmount("Invalid amount");
        }

        receiver.setAccountBalance(receiver.getAccountBalance() + amount);
        merchantRepo.save(receiver);

        TransactionModel transaction = new TransactionModel();
        transaction.setTransactionId(UUID.randomUUID().toString());
        transaction.setAmount(amount);
        transaction.setReceiver(receiver);
        transaction.setStatus(Status.SUCCESSFUL);

        transactionRepo.save(transaction);

        return TransactionMapper.mapToDto(transaction);
    }

    @Override
    public List<TransactionModel> getTransactionHistory(String accountNumber) {
            MerchantModel account = merchantRepo.findByAccountNumber(accountNumber)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            List<TransactionModel> transactions = transactionRepo
                    .findBySender_AccountNumberOrReceiver_AccountNumber(
                            accountNumber,
                            accountNumber
                    );

            return transactions;
        }
    }


