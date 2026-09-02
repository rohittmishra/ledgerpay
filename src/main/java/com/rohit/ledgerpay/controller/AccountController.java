package com.rohit.ledgerpay.controller;

import com.rohit.ledgerpay.dto.AccountResponse;
import com.rohit.ledgerpay.dto.OpenAccountRequest;
import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.repository.UserRepository;
import com.rohit.ledgerpay.security.SecurityUtil;
import com.rohit.ledgerpay.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;
    private final UserRepository userRepository;

    public AccountController(AccountService accountService, UserRepository userRepository) {
        this.accountService = accountService;
        this.userRepository = userRepository;
    }

    @PostMapping("/open")
    public ResponseEntity<AccountResponse> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserId));

        Account account = accountService.openAccount(user, request.getAccountType());
        return ResponseEntity.ok(new AccountResponse(account));
    }

    @GetMapping("/my-accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts() {
        Long currentUserId = SecurityUtil.getCurrentUserId();
        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + currentUserId));

        List<Account> accounts = accountService.getAccountsForUser(user);
        List<AccountResponse> response = accounts.stream()
                .map(AccountResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}