package nextstep.mvc.exception;

public class MvcException extends RuntimeException {

    private final Integer HttpStatus;

    public MvcException(String message, Integer httpStatus) {
        super(message);
        this.HttpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return HttpStatus;
    }
}
