package com.jini.auth_service.service;

import com.jini.auth_service.dto.LoginResponse;
import com.jini.auth_service.entity.User;
import com.jini.auth_service.exception.InvalidCredentialsException;
import com.jini.auth_service.exception.InvalidTokenException;
import com.jini.auth_service.repository.UserRepository;
import com.jini.auth_service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public AuthServiceImpl(UserService userService, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserRepository userRepository) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public LoginResult login(String email, String rawPassword) {
        User user = userService.getByEmail(email);
        String encodedPassword = user.getHashPassword();
        if(!passwordEncoder.matches(rawPassword, encodedPassword)){
            throw new InvalidCredentialsException();
        }
        String accessToken = jwtUtil.generateAccessToken(email);
        String refreshToken = jwtUtil.generateRefreshToken(email);
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
        return new LoginResult(accessToken, refreshToken);
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        if(!jwtUtil.isTokenValid(refreshToken)){
            throw new InvalidTokenException("Refresh token is invalid or expired");
        }
        String email = jwtUtil.extractEmail(refreshToken);
        User user = userService.getByEmail(email);
        if(!refreshToken.equals(user.getRefreshToken())){
            throw new InvalidTokenException("Refresh token doesn't match");
        }
        String newAccessToken = jwtUtil.generateAccessToken(email);
        return new LoginResponse(newAccessToken);
    }

    @Override
    public void logout(String email) {
        User user = userService.getByEmail(email);
        user.setRefreshToken(null);
        userRepository.save(user);
    }

}
