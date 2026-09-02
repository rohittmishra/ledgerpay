package com.rohit.ledgerpay.controller;

import com.rohit.ledgerpay.dto.DepositRequest;
import com.rohit.ledgerpay.dto.TransactionResponse;
import com.rohit.ledgerpay.dto.TransferRequest;
import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.Transaction;
import com.rohit.ledgerpay.repository.AccountRepository;
import com.rohit.ledgerpay.repository.TransactionRepository;
import com.rohit.ledgerpay.security.SecurityUtil;
import com.rohit.ledgerpay.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransactionController(TransactionService transactionService,
            AccountRepository accountRepository,
            TransactionRepository transactionRepository) {
        this.transactionService = transactionService;
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        Account fromAccount = accountRepository.findById(request.getFromAccountId())
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + request.getFromAccountId()));

        if (!fromAccount.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to transfer from this account");
        }

        Transaction transaction = transactionService.transferMoney(
                request.getFromAccountId(),
                request.getToAccountId(),
                request.getAmount());
        return ResponseEntity.ok(new TransactionResponse(transaction));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        Transaction transaction = transactionService.deposit(request.getAccountId(), request.getAmount());
        return ResponseEntity.ok(new TransactionResponse(transaction));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<TransactionResponse>> getHistory(@PathVariable Long accountId) {
        Long currentUserId = SecurityUtil.getCurrentUserId();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException("Account not found: " + accountId));

        if (!account.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You do not have permission to view this account's history");
        }

        List<Transaction> history = transactionRepository.findByFromAccountOrToAccount(account, account);
        List<TransactionResponse> response = history.stream()
                .map(TransactionResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}