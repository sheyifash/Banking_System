package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.ForgotPassWordReq;
import com.example.bankingsystem.Dto.ForgotPasswordResp;
import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Enum.Status;
import com.example.bankingsystem.Mapper.MerchantMapper;
import com.example.bankingsystem.Repo.MerchantRepo;
import com.example.bankingsystem.Utils.AccountNumberGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.DateTimeAtCreation;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthImpl implements AuthService{
    private final MerchantRepo merchantRepo;
    private final JWTService jwtService;
    private final AccountNumberGenerator accountNumberGenerator;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthImpl(MerchantRepo merchantRepo, JWTService jwtService, AccountNumberGenerator accountNumberGenerator, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.merchantRepo = merchantRepo;
        this.jwtService = jwtService;
        this.accountNumberGenerator = accountNumberGenerator;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResp Register(AuthReq req) {
        if (merchantRepo.existsByEmail(req.getEmail())){
            throw new RuntimeException("User already exists");
        }
        MerchantModel merchant = MerchantMapper.mapToEntity(req);
        merchant.setMessage("Merchant Successfully Created");
        merchant.setStatus(Status.SUCCESSFUL);
        merchant.setPassWord(passwordEncoder.encode(merchant.getPassword()));
        String accountNumber = accountNumberGenerator.generateUniqueAccountNumber();
        merchant.setAccountNumber(merchant.getAccountNumber(accountNumber));
        merchant.setCreatedAt(LocalDateTime.now());
        MerchantModel savedMerchant = merchantRepo.save(merchant);
        return MerchantMapper.mapToDto(savedMerchant);
    }

    @Override
    public AuthResp LogIn(AuthReq req) {
        String email = req.getEmail();
        String passWord = req.getPassWord();

        Optional<MerchantModel> merchant = merchantRepo.findByEmail(email);
        if (merchant.isEmpty()) {
            throw new BadCredentialsException("Invalid email or password");
        }
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, passWord)
            );
            String token = jwtService.generateToken(email);
            AuthResp resp = new AuthResp(token);
            resp.setMessage("Logged in successfully");
            resp.setStatus(Status.SUCCESSFUL);
            return resp;
        } catch (AuthenticationException ex) {
            // authentication failed (wrong password, etc.)
            throw new BadCredentialsException("Invalid email or password");
        }
    }

    @Override
    public ForgotPasswordResp ForgotPassWord(ForgotPassWordReq forgotPassWordReq) {
        String email = forgotPassWordReq.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        boolean exists = merchantRepo.existsByEmail(email);
        if (!exists) {
            throw new RuntimeException("User with this email does not exist");
        }

        // Generate a 9-character reset token
        String resetToken = UUID.randomUUID()
                .toString()
                .replace("-", "")   // remove dashes to avoid shorter substrings on some UUID variants
                .substring(0, 9);
        return new ForgotPasswordResp(resetToken);
    }
}
