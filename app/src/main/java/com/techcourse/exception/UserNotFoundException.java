package com.techcourse.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("존재하지 않는 사용자입니다.");
    }
}
