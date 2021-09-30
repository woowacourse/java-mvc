package com.techcourse.exception;

public class LoginFailedException extends AuthException {

    public LoginFailedException() {
        super("로그인에 실패하였습니다.");
    }
}
