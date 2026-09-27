package com.jini.auth_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @GetMapping("/me")
    public ResponseEntity<String> me(){
       String email = SecurityContextHolder.getContext().getAuthentication().getName();
       return ResponseEntity.ok("You are logged in as :" +email);

    }
}
