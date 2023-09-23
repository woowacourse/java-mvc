package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdaptor {
    boolean supports(Object handler);

    ModelAndView handle(HandlerExecution handler, HttpServletRequest request, HttpServletResponse response);
}
