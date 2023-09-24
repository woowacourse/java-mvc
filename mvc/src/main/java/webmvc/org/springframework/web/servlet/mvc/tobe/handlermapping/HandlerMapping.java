package webmvc.org.springframework.web.servlet.mvc.tobe.handlermapping;

import jakarta.servlet.http.HttpServletRequest;

public interface HandlerMapping {

    void initialize();

    boolean isHandleable(final HttpServletRequest request);

    Object getHandler(final HttpServletRequest request);
}
