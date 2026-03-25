package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.ForgotPassWordReq;
import com.example.bankingsystem.Dto.ForgotPasswordResp;
import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Mapper.MerchantMapper;
import com.example.bankingsystem.Repo.MerchantRepo;
import com.example.bankingsystem.Utils.AccountNumberGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService{
    private final MerchantRepo merchantRepo;
    private final JWTService jwtService;
    private final AccountNumberGenerator accountNumberGenerator;
    private final PasswordEncoder passWordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    public AuthServiceImpl(MerchantRepo merchantRepo, JWTService jwtService, AccountNumberGenerator accountNumberGenerator, PasswordEncoder passWordEncoder, AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.merchantRepo = merchantRepo;
        this.jwtService = jwtService;
        this.accountNumberGenerator = accountNumberGenerator;
        this.passWordEncoder = passWordEncoder;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthResp Register(AuthReq req) {
        if (merchantRepo.existsByEmail(req.getEmail())){
            throw new RuntimeException("User already exists");
        }
        MerchantModel merchant = MerchantMapper.mapToEntity(req);
        merchant.setPassWord(passWordEncoder.encode(merchant.getPassword()));
        String accountNumber = accountNumberGenerator.generateUniqueAccountNumber();
        merchant.setAccountNumber(merchant.getAccountNumber(accountNumber));
        MerchantModel savedMerchant = merchantRepo.save(merchant);
        return MerchantMapper.mapToDto(savedMerchant);
    }

    @Override
    public AuthResp LogIn(AuthReq req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        req.getEmail(),
                        req.getPassWord()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getEmail());
        String token = jwtService.generateToken(req.getEmail());
        return new AuthResp(token);
    }

    @Override
    public ForgotPasswordResp ForgotPassWord(ForgotPassWordReq forgotPassWordReq) {
        if (!merchantRepo.existsByEmail(forgotPassWordReq.getEmail())){
            throw new RuntimeException("user does not exist!");
        }
        String resetToken = UUID.randomUUID().toString().substring(0, 9);

        return new ForgotPasswordResp(resetToken);
    }
}
