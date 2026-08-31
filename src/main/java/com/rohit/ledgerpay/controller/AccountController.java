package com.rohit.ledgerpay.controller;

import com.rohit.ledgerpay.dto.OpenAccountRequest;
import com.rohit.ledgerpay.entity.Account;
import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.repository.UserRepository;
import com.rohit.ledgerpay.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<Account> openAccount(@Valid @RequestBody OpenAccountRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + request.getUserId()));

        Account account = accountService.openAccount(user, request.getAccountType());
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Account>> getAccountsForUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));

        List<Account> accounts = accountService.getAccountsForUser(user);
        return ResponseEntity.ok(accounts);
    }
}