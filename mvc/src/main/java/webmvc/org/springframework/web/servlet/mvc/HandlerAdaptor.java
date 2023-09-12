package webmvc.org.springframework.web.servlet.mvc;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdaptor {

    boolean supports(Object target);

    ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
