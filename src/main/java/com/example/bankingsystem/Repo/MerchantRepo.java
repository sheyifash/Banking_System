package com.example.bankingsystem.Repo;

import com.example.bankingsystem.Entity.MerchantModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MerchantRepo extends JpaRepository <MerchantModel, String> {
    MerchantModel findByUsername(String email);

    boolean existsByAccountNumber(String accountNumber);

   Optional <MerchantModel> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional <MerchantModel> findByAccountNumber(String accountNumber);
}
