package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class NoSuchRequestDispatcherException extends MvcException {

    private static final String MESSAGE = "요청 디스패처가 존재하지 않습니다.";
    private static final Integer HTTP_STATUS = 500;

    public NoSuchRequestDispatcherException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchRequestDispatcherException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
