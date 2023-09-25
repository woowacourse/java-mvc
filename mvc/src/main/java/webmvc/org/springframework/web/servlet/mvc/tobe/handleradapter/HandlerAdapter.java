package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.tobe.view.ModelAndView;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView adapt(HttpServletRequest request, final HttpServletResponse response, Object handler) throws Exception;
}
