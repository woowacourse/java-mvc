package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdaptor {
    boolean supports(Object handler);

    ModelAndView execute(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
