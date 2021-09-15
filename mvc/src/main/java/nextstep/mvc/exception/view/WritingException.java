package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class WritingException extends MvcException {

    private static final String MESSAGE = "응답을 입력할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public WritingException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private WritingException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
