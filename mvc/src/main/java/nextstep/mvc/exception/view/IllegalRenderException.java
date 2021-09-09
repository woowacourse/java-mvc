package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class IllegalRenderException extends MvcException {

    private static final String MESSAGE = "렌더링 할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public IllegalRenderException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private IllegalRenderException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
