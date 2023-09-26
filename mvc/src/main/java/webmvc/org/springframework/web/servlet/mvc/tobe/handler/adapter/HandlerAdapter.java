package webmvc.org.springframework.web.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdapter {

    ModelAndView execute(final HttpServletRequest req, final HttpServletResponse res, final Object handler) throws Exception;

    boolean isSupported(final Object handler);
}
