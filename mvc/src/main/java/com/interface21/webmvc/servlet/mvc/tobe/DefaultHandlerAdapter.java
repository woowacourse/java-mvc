package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class DefaultHandlerAdapter implements HandlerAdapter {

    private static final String DEFAULT_HANDLER_METHOD_NAME = "handle";

    @Override
    public boolean isSupports(Object handler) {
        return true;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {
        return (ModelAndView) handler.getClass()
            .getDeclaredMethod(DEFAULT_HANDLER_METHOD_NAME)
            .invoke(handler);
    }
}
