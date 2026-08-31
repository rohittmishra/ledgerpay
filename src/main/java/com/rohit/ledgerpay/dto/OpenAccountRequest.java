package com.rohit.ledgerpay.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAccountRequest {

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotBlank(message = "Account type is required")
    private String accountType;
}