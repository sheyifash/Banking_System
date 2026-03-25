package com.example.bankingsystem.Dto;

import lombok.Data;

@Data
public class AuthReq {
    private String firstName;
    private String lastName;
    private String email;
    private String passWord;
    private String mobileNumber;
    private String pin;
}
