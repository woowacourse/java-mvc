package com.techcourse.exception;

public class AppException extends RuntimeException {

    private final Integer httpStatus;

    public AppException(String message, Integer httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
