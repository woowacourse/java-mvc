package nextstep.web.exception.support;

import nextstep.web.exception.WebException;

public class RequestMethodException extends WebException {

    private static final String MESSAGE = "존재하지 않는 요청 타입입니다.";
    private static final Integer HTTP_STATUS = 400;

    public RequestMethodException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private RequestMethodException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
