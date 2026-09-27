package com.jini.auth_service.exception;

public class EmailAlreadyExistException extends RuntimeException{
    public EmailAlreadyExistException(String email) {
        super("This email is already registered : " +email);
    }
}
