package nextstep.mvc.exception.tobe;

import nextstep.mvc.exception.MvcException;

public class IllegalMethodException extends MvcException {

    private static final String MESSAGE = "메소드를 실행할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public IllegalMethodException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private IllegalMethodException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
