package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class RenderingException extends MvcException {

    private static final String MESSAGE = "렌더링 할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public RenderingException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private RenderingException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
