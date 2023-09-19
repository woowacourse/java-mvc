package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.bind.annotation.RequestMethod;

public interface HandlerMapping {

    void initialize();

    boolean support(final HttpServletRequest request);

    Object getHandlerExecution(final HttpServletRequest request);

    default HandlerKey getHandlerKey(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());

        return new HandlerKey(requestUri, requestMethod);
    }
}
