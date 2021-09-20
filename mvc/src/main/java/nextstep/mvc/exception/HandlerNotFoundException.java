package nextstep.mvc.exception;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerNotFoundException extends MvcException {

    public HandlerNotFoundException(HttpServletRequest request) {
        super(String.format("Unable to find handler. [uri]: %s, [method]: %s", request.getRequestURI(), request.getMethod()));
    }
}
