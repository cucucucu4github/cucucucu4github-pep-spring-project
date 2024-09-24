package com.example.exception;

public class MessageIdNotExistsException extends Exception{
    public MessageIdNotExistsException(String msg){
        super(msg);
    }
}
