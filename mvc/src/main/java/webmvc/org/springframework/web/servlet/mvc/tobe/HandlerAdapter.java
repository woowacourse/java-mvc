package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {
    Object execute(final Object handler, final HttpServletRequest req, final HttpServletResponse res);

    boolean isSupport(final Object handler);
}
