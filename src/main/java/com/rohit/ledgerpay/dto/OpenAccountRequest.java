package com.rohit.ledgerpay.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAccountRequest {

    @NotBlank(message = "Account type is required")
    private String accountType;
}