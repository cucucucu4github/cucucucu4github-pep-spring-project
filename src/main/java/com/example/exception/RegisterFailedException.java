package com.example.exception;

public class RegisterFailedException extends Exception {
    
    public RegisterFailedException(String message) {
        super(message);  // 直接使用传递的错误信息
    }
}
