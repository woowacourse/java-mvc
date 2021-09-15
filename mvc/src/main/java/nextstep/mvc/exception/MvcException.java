package nextstep.mvc.exception;

public class MvcException extends RuntimeException {

    private final Integer httpStatus;

    public MvcException(String message, Integer httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
