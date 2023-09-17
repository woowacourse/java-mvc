package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping<T> {

    void initialize();

    T getHandler(final HttpServletRequest request);
}
