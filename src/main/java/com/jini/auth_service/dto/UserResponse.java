package com.jini.auth_service.dto;

import com.jini.auth_service.entity.User;

import java.time.LocalDateTime;
import java.util.Set;

public record UserResponse (
        Long id,
        String email,
        Set<String> roles,
        Boolean enabled,
        LocalDateTime createdAt
)
{}
