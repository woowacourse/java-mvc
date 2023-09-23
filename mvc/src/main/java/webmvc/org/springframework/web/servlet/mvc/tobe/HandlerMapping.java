package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import web.org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

public interface HandlerMapping {

    void initialize();

    boolean support(final HttpServletRequest request);

    Object getHandlerExecution(final HttpServletRequest request);

    List<HandlerKey> getHandlerKeys();

    default HandlerKey getHandlerKey(final HttpServletRequest request) {
        final String requestUri = request.getRequestURI();
        final RequestMethod requestMethod = RequestMethod.from(request.getMethod());

        return new HandlerKey(requestUri, requestMethod);
    }
}
