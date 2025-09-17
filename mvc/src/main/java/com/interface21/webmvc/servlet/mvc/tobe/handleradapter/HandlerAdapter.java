package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdapter {

    boolean supports(final Object handler);

    ModelAndView execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception;
}
