package com.example.exception;

public class LoginFailedException extends Exception {
    public LoginFailedException(String message){
        super(message);
    }
}
