package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.*;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public AuthResp register(AuthReq req);
    public LogInRes logIn(LogInReq req);
    public ForgotPasswordResp forgotPassWord(ForgotPassWordReq forgotPassWordReq);

}
