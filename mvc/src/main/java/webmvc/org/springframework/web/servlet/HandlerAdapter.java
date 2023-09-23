package webmvc.org.springframework.web.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean isSupport(Object handler);

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception;
}
