package webmvc.org.springframework.web.servlet.mvc.frontcontroller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isSupporting(final Object handler);

    ModelAndView handle(final HttpServletRequest request,
                        final HttpServletResponse response,
                        final Object handler) throws Exception;
}
