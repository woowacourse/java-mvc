package com.techcourse.exception;

public class NoSuchUserException extends AppException{

    private static final String MESSAGE = "사용자가 존재하지 않습니다.";
    private static final Integer HTTP_STATUS = 400;

    public NoSuchUserException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchUserException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
