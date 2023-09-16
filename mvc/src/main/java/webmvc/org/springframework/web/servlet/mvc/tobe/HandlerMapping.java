package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping<T> {

    void initialize();

    T getHandler(final HttpServletRequest request);
}
