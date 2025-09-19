package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean support(final Object handler);

    ModelAndView handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) throws Exception;

}
