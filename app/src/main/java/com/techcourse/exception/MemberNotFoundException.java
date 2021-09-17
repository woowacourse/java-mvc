package com.techcourse.exception;

public class MemberNotFoundException extends RuntimeException{

    public MemberNotFoundException(MemberExceptionMessage message) {
        super(message.getMessage());
    }
}
