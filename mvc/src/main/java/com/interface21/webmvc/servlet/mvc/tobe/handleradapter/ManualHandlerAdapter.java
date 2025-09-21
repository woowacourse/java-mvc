package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter{
    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(Object handler, HttpServletRequest request, HttpServletResponse response) {
        try {
            return ((Controller) handler).execute(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
