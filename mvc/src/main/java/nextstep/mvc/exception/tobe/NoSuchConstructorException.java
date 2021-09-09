package nextstep.mvc.exception.tobe;

import nextstep.mvc.exception.MvcException;

public class NoSuchConstructorException extends MvcException {

    private static final String MESSAGE = "생성자가 존재하지 않습니다.";
    private static final Integer HTTP_STATUS = 500;

    public NoSuchConstructorException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchConstructorException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
