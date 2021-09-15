package nextstep.mvc.exception.controller;

import nextstep.mvc.exception.MvcException;

public class IllegalNewInstanceException extends MvcException {

    private static final String MESSAGE = "객체를 생성할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public IllegalNewInstanceException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private IllegalNewInstanceException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
