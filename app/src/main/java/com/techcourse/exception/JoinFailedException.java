package com.techcourse.exception;

public class JoinFailedException extends BadRequestException {

    public JoinFailedException() {
        super("중복된 계정명은 사용할 수 없습니다.");
    }
}
