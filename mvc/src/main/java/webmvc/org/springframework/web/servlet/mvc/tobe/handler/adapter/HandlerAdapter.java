package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    Object execute(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception;

    boolean isSupported(final Object handler);
}
