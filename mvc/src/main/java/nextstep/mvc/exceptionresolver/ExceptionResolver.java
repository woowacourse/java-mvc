package nextstep.mvc.exceptionresolver;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface ExceptionResolver {
    void resolve(Exception exception, HttpServletRequest httpRequest, HttpServletResponse httpResponse);
    boolean supportsException(Exception exception);
}
