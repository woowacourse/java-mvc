package webmvc.org.springframework.web.servlet.mvc.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    Object getHandler(final HttpServletRequest request);
}
