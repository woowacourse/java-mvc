package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean supports(final Object handler);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
