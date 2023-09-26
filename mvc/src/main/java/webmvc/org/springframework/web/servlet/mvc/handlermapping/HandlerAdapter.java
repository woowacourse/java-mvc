package webmvc.org.springframework.web.servlet.mvc.handlermapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.org.springframework.web.Handler;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean supports(Handler handler);

    ModelAndView handle(Handler handler, HttpServletRequest request, HttpServletResponse response) throws Exception;
}
