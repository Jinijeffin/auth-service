package com.jini.auth_service.service;


import com.jini.auth_service.dto.LoginResponse;

public interface AuthService {
    record LoginResult(String accessToken, String refreshToken) {}
    LoginResult login(String email, String rawPassword);
    LoginResponse refreshAccessToken(String refreshToken);
    void logout(String email);
}
