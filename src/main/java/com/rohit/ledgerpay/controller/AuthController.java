package com.rohit.ledgerpay.controller;

import com.rohit.ledgerpay.dto.LoginRequest;
import com.rohit.ledgerpay.dto.LoginResponse;
import com.rohit.ledgerpay.dto.UserResponse;
import com.rohit.ledgerpay.entity.User;
import com.rohit.ledgerpay.security.JwtUtil;
import com.rohit.ledgerpay.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        User user = userService.validateCredentials(request.getEmail(), request.getPassword());
        String token = jwtUtil.generateToken(user.getId(), user.getEmail());
        return ResponseEntity.ok(new LoginResponse(token, new UserResponse(user)));
    }
}