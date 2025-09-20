package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    ModelAndView handle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler
    ) throws Exception;

    boolean canHandle(final Object handler);
}
