package com.example.bankingsystem.Entity;

import com.example.bankingsystem.Enum.Status;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class TransactionModel {
    @Id
    private String transactionId;
    @Column
    private String SenderName;
    @Column
    private String receiverName;
    @Column
    private long amount;
    @Column(unique = true)
    private String accountNumber;
    @Column
    private long accountBalance;
    @Column
    private String narration;
    @Column(unique = true)
    private String idempotencyKey;
    @Column
    private LocalDateTime expiresAt;
    @Column
    private LocalDateTime transactionTime;
    @Column
    private Status status;
    @Column
    private String message;

    public void setSender(MerchantModel sender) {
    }

    public void setReceiver(MerchantModel receiver) {
    }
}
