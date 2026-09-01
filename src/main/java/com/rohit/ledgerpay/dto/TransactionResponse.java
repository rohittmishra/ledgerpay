package com.rohit.ledgerpay.dto;

import com.rohit.ledgerpay.entity.Transaction;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class TransactionResponse {
    private final Long id;
    private final AccountResponse fromAccount;
    private final AccountResponse toAccount;
    private final BigDecimal amount;
    private final String type;
    private final String status;
    private final LocalDateTime createdAt;

    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.fromAccount = transaction.getFromAccount() != null
                ? new AccountResponse(transaction.getFromAccount())
                : null;
        this.toAccount = transaction.getToAccount() != null
                ? new AccountResponse(transaction.getToAccount())
                : null;
        this.amount = transaction.getAmount();
        this.type = transaction.getType();
        this.status = transaction.getStatus();
        this.createdAt = transaction.getCreatedAt();
    }
}