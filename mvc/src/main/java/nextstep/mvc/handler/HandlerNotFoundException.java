package nextstep.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;

public class HandlerNotFoundException extends RuntimeException {

    public HandlerNotFoundException(HttpServletRequest request) {
        super(String.format("Unable to find handler. [uri]: %s, [method]: %s", request.getRequestURI(), request.getMethod()));
    }
}
