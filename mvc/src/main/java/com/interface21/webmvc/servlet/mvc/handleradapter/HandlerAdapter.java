package com.interface21.webmvc.servlet.mvc.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(Object handler);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception;
}
