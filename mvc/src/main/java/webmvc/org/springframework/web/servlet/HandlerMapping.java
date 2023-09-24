package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    Object getHandler(final HttpServletRequest request);

    void initialize();
}
