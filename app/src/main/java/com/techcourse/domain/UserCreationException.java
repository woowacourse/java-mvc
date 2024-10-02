package com.techcourse.domain;

public class UserCreationException extends RuntimeException {
    public UserCreationException() {
        super("유저 정보를 모두 입력해주세요.");
    }
}
