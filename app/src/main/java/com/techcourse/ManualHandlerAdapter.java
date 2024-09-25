package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.mvc.tobe.HandlerAdapter;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public String handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return ((Controller) handler).execute(request, response);
    }
}
