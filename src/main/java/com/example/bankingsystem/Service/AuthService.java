package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.ForgotPassWordReq;
import com.example.bankingsystem.Dto.ForgotPasswordResp;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {
    public AuthResp Register(AuthReq req);
    public AuthResp LogIn(AuthReq req);
    public ForgotPasswordResp ForgotPassWord(ForgotPassWordReq forgotPassWordReq);

}
