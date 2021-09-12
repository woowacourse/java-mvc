package nextstep.mvc.exception;

public class NoSuchHandlerAdapterException extends MvcException{

    private static final String MESSAGE = "핸들러 어댑터를 찾을 수 없습니다.";
    private static final Integer HTTP_STATUS = 500;

    public NoSuchHandlerAdapterException() {
        this(MESSAGE, HTTP_STATUS);
    }

    private NoSuchHandlerAdapterException(String message, Integer httpStatus) {
        super(message, httpStatus);
    }
}
