package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

public interface HandlerAdaptor {

    boolean isSame(final Object handler);

    ModelAndView execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}
