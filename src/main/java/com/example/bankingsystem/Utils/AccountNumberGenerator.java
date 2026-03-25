package com.example.bankingsystem.Utils;

import java.security.SecureRandom;

import com.example.bankingsystem.Repo.MerchantRepo;
import org.springframework.stereotype.Component;

@Component
public class AccountNumberGenerator {

    private final MerchantRepo merchantRepo;
    private static final String PREFIX = "208";
    private final SecureRandom random = new SecureRandom();

    public AccountNumberGenerator(MerchantRepo merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    public String generate() {
        long number = 1000000L + (long)(random.nextDouble() * 9000000L);
        return PREFIX + number;
    }

    public String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            accountNumber = generate();
        } while (merchantRepo.existsByAccountNumber(accountNumber));

        return accountNumber;
    }
}