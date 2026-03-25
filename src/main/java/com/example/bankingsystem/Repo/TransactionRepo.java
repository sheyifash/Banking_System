package com.example.bankingsystem.Repo;

import com.example.bankingsystem.Entity.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepo extends JpaRepository <TransactionModel, String> {


    Optional<TransactionModel> findByIdempotencyKeyAndExpiresAtAfter(String idempotencyKey, LocalDateTime now);

    void deleteByExpiresAtBefore(LocalDateTime now);

    List<TransactionModel> findBySender_AccountNumberOrReceiver_AccountNumber(String accountNumber, String accountNumber1);
}
