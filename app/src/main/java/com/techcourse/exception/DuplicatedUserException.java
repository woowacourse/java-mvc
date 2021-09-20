package com.techcourse.exception;

public class DuplicatedUserException extends RuntimeException {
    public DuplicatedUserException() {
    }

    public DuplicatedUserException(String message) {
        super(message);
    }

    public DuplicatedUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
