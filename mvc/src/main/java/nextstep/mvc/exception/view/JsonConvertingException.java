package nextstep.mvc.exception.view;

import nextstep.mvc.exception.MvcException;

public class JsonConvertingException extends MvcException {

    private static final String MESSAGE = "Json으로 변환할 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public JsonConvertingException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private JsonConvertingException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
