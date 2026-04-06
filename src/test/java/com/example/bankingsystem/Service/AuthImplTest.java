package com.example.bankingsystem.Service;

import com.example.bankingsystem.Dto.AuthReq;
import com.example.bankingsystem.Dto.AuthResp;
import com.example.bankingsystem.Dto.LogInReq;
import com.example.bankingsystem.Dto.LogInRes;
import com.example.bankingsystem.Entity.MerchantModel;
import com.example.bankingsystem.Enum.Status;
import com.example.bankingsystem.Mapper.MerchantMapper;
import com.example.bankingsystem.Repo.MerchantRepo;
import com.example.bankingsystem.Utils.AccountNumberGenerator;
import org.assertj.core.api.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthImplTest {

    @Mock
    private MerchantRepo merchantRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @Mock
    private JWTService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthImpl authService;

    private AuthReq req;

    @BeforeEach
    void setUp() {
        req = new AuthReq();
        req.setFirstName("Sheyi");
        req.setLastName("Sheyim");
        req.setEmail("sheyi@gmail.com");
        req.setMobileNumber("837830999");
        req.setPassword("Password@123");
        req.setPin("3657");
    }

    @Test
    void testRegisterNewUserAndReturnSuccessfulResponse() {
        // Mock repository and utilities
        when(merchantRepo.existsByEmail(req.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded-password");
        when(accountNumberGenerator.generateUniqueAccountNumber()).thenReturn("2087643789");
        when(merchantRepo.save(any(MerchantModel.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Mock MerchantMapper to return a dummy response
        AuthResp dummyResp = new AuthResp();
        dummyResp.setAccountNumber("2087643789");
        dummyResp.setMessage("Merchant Successfully Created");
        dummyResp.setStatus(Status.CREATED);
        try (MockedStatic<MerchantMapper> mockedStatic = Mockito.mockStatic(MerchantMapper.class)) {
            mockedStatic.when(() -> MerchantMapper.mapToEntity(any(AuthReq.class)))
                    .thenReturn(new MerchantModel());
            mockedStatic.when(() -> MerchantMapper.mapToDto(any(MerchantModel.class)))
                    .thenReturn(dummyResp);

            // Call the method under test
            AuthResp resp = authService.register(req);

            // Verify results
            assertNotNull(resp);
            assertEquals("2087643789", resp.getAccountNumber());
            assertEquals("Merchant Successfully Created", resp.getMessage());
            assertEquals(Status.CREATED, resp.getStatus());

            // Optional: verify interactions
            verify(passwordEncoder).encode("Password@123");
            verify(accountNumberGenerator).generateUniqueAccountNumber();
            verify(merchantRepo).existsByEmail("sheyi@gmail.com");
        }
    }

    @Test
    void registerNewUserAndReturnExistingUser(){
        req = new AuthReq();
        req.setFirstName("Sheyi");
        req.setLastName("Sheyim");
        req.setEmail("sheyi@gmail.com");
        req.setMobileNumber("837830999");
        req.setPassword("Password@123");
        req.setPin("3657");

        when(merchantRepo.existsByEmail(req.getEmail())).thenReturn(true);
    }

 @Test
    void logInAndReturnSuccessfulResponseTest() {
        //Arrange
        LogInReq req = new LogInReq();
       req.setEmail("sheyi@gmail.com");
        req.setPassword("Password@123");

        MerchantModel merchantModel = new MerchantModel();
        merchantModel.setEmail(req.getEmail());
        merchantModel.setPassword("encoded-password");

        when(merchantRepo.findByEmail(req.getEmail())).thenReturn(Optional.of(merchantModel));
        String token = "mockedjwteyoeijdnjkfelwm43456jk7l6324546yte";
        when(jwtService.generateToken("sheyi@gmail.com")).thenReturn(token);

     Authentication auth = mock(Authentication.class);
     when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
             .thenReturn(auth);
     LogInRes dummyresp = new LogInRes(token);
     dummyresp.setMessage("Logged in successfully");
     dummyresp.setStatus(Status.SUCCESSFUL);
     dummyresp.setToken(token);
     dummyresp.setLoggedInAt(LocalDateTime.now());
        //Act
     LogInRes res = authService.logIn(req);

     //Assert
     assertNotNull(res);
     assertEquals(token, res.getToken());
     assertEquals("Logged in successfully", res.getMessage());
     assertEquals(Status.SUCCESSFUL, res.getStatus());
     assertNotNull(res.getLoggedInAt());
    }
}