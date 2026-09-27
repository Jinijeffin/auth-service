package com.jini.auth_service.controller;

import com.jini.auth_service.dto.*;
import com.jini.auth_service.entity.User;
import com.jini.auth_service.mapper.UserMapper;
import com.jini.auth_service.security.LoginRateLimiter;
import com.jini.auth_service.service.AuthService;
import com.jini.auth_service.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthService authService;
    private final LoginRateLimiter loginRateLimiter;

    public AuthController(UserService userService, AuthService authService, LoginRateLimiter loginRateLimiter) {
        this.userService = userService;
        this.authService = authService;
        this.loginRateLimiter = loginRateLimiter;
    }


    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody RegisterRequest registerRequest){
        User user = userService.registerUser(registerRequest.email(),registerRequest.password());
        UserResponse response = UserMapper.toResponse(user);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest request){
        String clientIp = request.getRemoteAddr();
        if(!loginRateLimiter.isAllowed(clientIp)){
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error","Too many login attempts. Please try again later."));
        }
        AuthService.LoginResult result = authService.login(loginRequest.email(), loginRequest.password());

        ResponseCookie cookie = ResponseCookie.from("refreshToken", result.refreshToken())
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new LoginResponse(result.accessToken()));
    }
    @PostMapping("/refresh")
    public ResponseEntity<LoginResponse> refreshAccessToken(@CookieValue("refreshToken") String refreshToken){
        LoginResponse response = authService.refreshAccessToken(refreshToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
             authService.logout(email);
        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/api/auth")
                .maxAge(0)
                .sameSite("Strict")
                .build();
        return ResponseEntity.noContent()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .build();
    }
}
