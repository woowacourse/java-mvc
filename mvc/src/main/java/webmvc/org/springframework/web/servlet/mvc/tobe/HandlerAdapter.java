package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isSupport(Object handler);

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
