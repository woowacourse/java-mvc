package com.techcourse.exception;

public class ReflectionException extends AppException {

    private static final String MESSAGE = "리플렉션 과정에서 문제가 발생했습니다.";
    private static final Integer HTTP_STATUS = 500;

    public ReflectionException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private ReflectionException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
