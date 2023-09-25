package webmvc.org.springframework.web.servlet.mvc.handlerAdapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isSupport(final Object handler);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}
