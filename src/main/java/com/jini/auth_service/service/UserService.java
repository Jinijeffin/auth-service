package com.jini.auth_service.service;

import com.jini.auth_service.entity.User;


public interface UserService {

    User registerUser(String email, String rawPassword);

    User getByEmail(String email);

}
