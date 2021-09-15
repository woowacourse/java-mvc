package com.techcourse.exception;

public class OutOfUserIdException extends AppException{

    private static final String MESSAGE = "사용자를 더 이상 생성할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public OutOfUserIdException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private OutOfUserIdException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
