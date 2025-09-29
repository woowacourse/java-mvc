package com.interface21.webmvc.servlet.processor;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerProcessor {

    boolean supports(final Object handler);

    ModelAndView process(final Object handler, final HttpServletRequest request, final HttpServletResponse response);
}
