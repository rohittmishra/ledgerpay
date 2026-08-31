package com.rohit.ledgerpay.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rohit.ledgerpay.dto.RegisterRequest;
import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@Valid @RequestBody RegisterRequest request) {
        User savedUser = userService.registerUser(
            request.getName(),
            request.getEmail(),
            request.getPassword()
        );
        return ResponseEntity.ok(savedUser);
    }
}
