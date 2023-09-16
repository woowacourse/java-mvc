package com.techcourse.exception;

public class HandlerNotFoundException extends RuntimeException{

    public HandlerNotFoundException(String e) {
        super(e);
    }
}
