package nextstep.web.exception;

public class WebException extends RuntimeException {

    private final Integer httpStatus;

    public WebException(String message, Integer httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public Integer getHttpStatus() {
        return httpStatus;
    }
}
