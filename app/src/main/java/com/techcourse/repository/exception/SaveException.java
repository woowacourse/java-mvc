package com.techcourse.repository.exception;

public class SaveException extends RuntimeException {
    private static final String MESSAGE = "User 저장에 실패하였습니다.";

    public SaveException() {
        super(MESSAGE);
    }
}
