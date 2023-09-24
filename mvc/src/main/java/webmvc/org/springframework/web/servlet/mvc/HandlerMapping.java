package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();
    boolean containsHandler(final HttpServletRequest request);
    Object getHandler(final HttpServletRequest request);
}
