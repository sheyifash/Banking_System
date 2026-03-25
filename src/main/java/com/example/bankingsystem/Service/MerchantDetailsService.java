package com.example.bankingsystem.Service;

import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Repo.MerchantRepo;
import com.example.bankingsystem.Repo.MerchantRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MerchantDetailsService implements UserDetailsService {
    private final MerchantRepo merchantRepo;

    public MerchantDetailsService(MerchantRepo merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MerchantModel merchant = merchantRepo.findByUsername(email);
        if (merchant == null){
            System.out.println("Merchant not found");
            throw new UsernameNotFoundException("Merchant not found");
        }
        return merchant;
    }
}
