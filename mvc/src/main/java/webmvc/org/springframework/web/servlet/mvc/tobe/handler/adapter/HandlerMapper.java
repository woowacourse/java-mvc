package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapper {

    void initialize();

    Object getController(final HttpServletRequest req);
}
