package nextstep.mvc.exception;

public class NoSuchHandlerException extends MvcException {

    private static final String MESSAGE = "핸들러를 찾을 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public NoSuchHandlerException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchHandlerException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
