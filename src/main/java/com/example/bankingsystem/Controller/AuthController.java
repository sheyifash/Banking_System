package com.example.bankingsystem.Controller;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.ForgotPassWordReq;
import com.example.bankingsystem.Dto.ForgotPasswordResp;
import com.example.bankingsystem.Service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResp> register(@RequestBody AuthReq req) {
        return ResponseEntity.ok(authService.Register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResp> login(@RequestBody AuthReq req) {
        return ResponseEntity.ok(authService.LogIn(req));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ForgotPasswordResp> forgotPassword(
            @RequestBody ForgotPassWordReq req
    ) {
        return ResponseEntity.ok(authService.ForgotPassWord(req));
    }
}