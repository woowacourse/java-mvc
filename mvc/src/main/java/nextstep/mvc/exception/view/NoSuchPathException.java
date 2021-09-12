package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class NoSuchPathException extends MvcException {

    private static final String MESSAGE = "경로가 존재하지 않습니다.";
    private static final Integer HTTP_STATUS = 404;

    public NoSuchPathException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchPathException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
