package com.example.bankingsystem.Entity;

import com.example.bankingsystem.Enum.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@Table(name = "UserAuthTable")
@EntityListeners(AuditingEntityListener.class)
public class MerchantModel implements UserDetails {
    @Id
    @NotBlank
    private String accountNumber;
    @Column
    @NotBlank
    private String firstName;
    @Column
    @NotBlank
    private String lastName;
    @Column
    @NotBlank
    private String mobileNumber;
    @Column
    @NotBlank
    private String email;
    @Column
    @NotBlank
    private String password;
    @Column
    @NotBlank
    private String pin;
    @Column
    private String token;
    @Column
    private long accountBalance;
    @Column
    @Enumerated(EnumType.ORDINAL)
    private Status status;
    @Column
    private String message;
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public @NotBlank String getAccountNumber(String accountNumber) {
        return accountNumber;
    }
}
