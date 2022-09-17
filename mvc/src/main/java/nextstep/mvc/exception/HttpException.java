package nextstep.mvc.exception;

import nextstep.web.support.HttpStatus;

public abstract class HttpException extends RuntimeException {

    private final HttpStatus status;

    public HttpException(final String message, final HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
