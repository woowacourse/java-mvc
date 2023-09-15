package webmvc.org.springframework.web.servlet.mvc.tobe.handler.mapper;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize();

    Object getHandler(final HttpServletRequest req);
}
