package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize();

    Object getHandler(final HttpServletRequest request);
}
