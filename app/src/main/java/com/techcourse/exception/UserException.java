package com.techcourse.exception;

public class UserException extends RuntimeException{

    public UserException(String account){
        super("User not found" + account);
    }
}
