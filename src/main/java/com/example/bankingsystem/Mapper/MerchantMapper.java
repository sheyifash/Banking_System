package com.example.bankingsystem.Mapper;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Entity.MerchantModel;
import org.springframework.stereotype.Component;

@Component
public class MerchantMapper{
    public static AuthResp mapToDto(MerchantModel auth){
      return AuthResp.builder()
              .status(auth.getStatus())
              .token(auth.getToken())
              .accountNumber(auth.getAccountNumber())
              .accountBalance(auth.getAccountBalance())
              .createdAt(auth.getCreatedAt())
              .message(auth.getMessage())
        .build();
    }
    public static MerchantModel mapToEntity(AuthReq request){
        MerchantModel authModel = new MerchantModel();
        authModel.setFirstName(request.getFirstName());
        authModel.setLastName(request.getLastName());
        authModel.setEmail(request.getEmail());
        authModel.setMobileNumber(request.getMobileNumber());
        authModel.setPassword(request.getPassword());
        authModel.setPin(request.getPin());
        return authModel;
    }
}
