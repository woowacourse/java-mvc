package com.interface21.webmvc.servlet.mvc.handleradaptor;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface HandlerAdaptor {

    boolean support(final Object handler);

    ModelAndView handle(final Object handler,
                        final HttpServletRequest request,
                        final HttpServletResponse response) throws Exception;
}
