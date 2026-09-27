package com.jini.auth_service.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
            @NotBlank(message = "email must not be blank")
            String email,
            @NotBlank(message = "password must not be blank")
            String password
    ){}

