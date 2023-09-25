package webmvc.org.springframework.web.servlet.mvc.tobe.handler_mapping;

import jakarta.servlet.http.HttpServletRequest;
import webmvc.org.springframework.web.servlet.mvc.tobe.handler.Handler;

public interface HandlerMapping {

    public void initialize();

    public Handler getHandler(final HttpServletRequest request);
}
