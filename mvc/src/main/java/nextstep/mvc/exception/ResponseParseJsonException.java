package nextstep.mvc.exception;

public class ResponseParseJsonException extends RuntimeException {
    public ResponseParseJsonException() {
    }

    public ResponseParseJsonException(String message) {
        super(message);
    }

    public ResponseParseJsonException(String message, Throwable cause) {
        super(message, cause);
    }
}
