package webmvc.org.springframework.web.servlet.mvc.tobe.handler;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize() throws Exception;

    Object getHandler(final HttpServletRequest request);
}
