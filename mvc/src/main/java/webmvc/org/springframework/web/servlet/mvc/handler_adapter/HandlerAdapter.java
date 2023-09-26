package webmvc.org.springframework.web.servlet.mvc.handler_adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean support(Object handler);

    ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}

