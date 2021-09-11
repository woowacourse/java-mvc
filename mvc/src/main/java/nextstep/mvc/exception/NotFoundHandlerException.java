package nextstep.mvc.exception;

import jakarta.servlet.http.HttpServletRequest;

public class NotFoundHandlerException extends RuntimeException {

    public NotFoundHandlerException(HttpServletRequest request) {
        super("해당 요청을 처리할 Handler가 존재하지 않습니다. 요청값: " + request.getMethod() + " " + request.getRequestURI());
    }
}
