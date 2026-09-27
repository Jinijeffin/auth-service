package com.jini.auth_service.service;

import com.jini.auth_service.entity.User;
import com.jini.auth_service.exception.EmailAlreadyExistException;
import com.jini.auth_service.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User registerUser(String email, String rawPassword) {
        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyExistException(email);
        }
        User user = new User();
        user.setEmail(email);
        user.setHashPassword(passwordEncoder.encode(rawPassword));
        user.setRoles(Set.of("ROLE_USER"));
        user.setEnabled(true);

        return userRepository.save(user);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(()->new IllegalArgumentException("No user found with email"+ email));
    }
}
