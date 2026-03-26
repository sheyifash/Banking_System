package com.example.bankingsystem.Controller;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.ForgotPassWordReq;
import com.example.bankingsystem.Dto.ForgotPasswordResp;
import com.example.bankingsystem.Service.AuthImpl;
import com.example.bankingsystem.Service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for registration, login and forgot password")
public class AuthController {

    private final AuthService authService;
    private final AuthImpl authImpl;

    public AuthController(AuthService authService, AuthImpl authImpl) {
        this.authService = authService;
        this.authImpl = authImpl;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new merchant",
            description = "Creates a new merchant"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "User already exists")
    })

    public AuthResp Register(@RequestBody AuthReq req){
        return authImpl.Register(req);
    }

    @PostMapping("/login")
    public AuthResp login(@RequestBody AuthReq req) {
        return authImpl.LogIn(req);
    }

    @PostMapping("/forgot-password")
    public ForgotPasswordResp forgotPassword(
            @RequestBody ForgotPassWordReq req
    ) {
        return authImpl.ForgotPassWord(req);
    }
}