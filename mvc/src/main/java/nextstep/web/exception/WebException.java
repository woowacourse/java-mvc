package nextstep.web.exception;

public class WebException extends RuntimeException {

    private final Integer HttpStatus;

    public WebException(String message, Integer httpStatus) {
        super(message);
        this.HttpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return HttpStatus;
    }
}
