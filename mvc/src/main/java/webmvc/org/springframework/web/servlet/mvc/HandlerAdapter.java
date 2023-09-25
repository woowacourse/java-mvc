package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean support(Object handler);

    ModelAndView invoke(Object handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
