package com.techcourse.exception;

public class UserNotFoundException extends AuthException{
    public UserNotFoundException() {
        super("회원을 찾을 수 없습니다.");
    }
}
