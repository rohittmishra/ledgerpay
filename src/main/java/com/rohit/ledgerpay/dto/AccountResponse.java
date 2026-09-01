package com.rohit.ledgerpay.dto;

import com.rohit.ledgerpay.entity.Account;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class AccountResponse {
    private final Long id;
    private final UserResponse user;
    private final String accountNumber;
    private final BigDecimal balance;
    private final String accountType;
    private final LocalDateTime createdAt;

    public AccountResponse(Account account) {
        this.id = account.getId();
        this.user = new UserResponse(account.getUser());
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.accountType = account.getAccountType();
        this.createdAt = account.getCreatedAt();
    }
}