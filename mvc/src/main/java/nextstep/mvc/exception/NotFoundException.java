package nextstep.mvc.exception;

import nextstep.web.support.HttpStatus;

public class NotFoundException extends HttpException {

    public NotFoundException(final String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
