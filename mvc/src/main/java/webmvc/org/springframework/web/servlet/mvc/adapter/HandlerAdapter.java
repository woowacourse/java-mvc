package webmvc.org.springframework.web.servlet.mvc.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    boolean isHandlerAdapter(final Object object);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) ;
}
