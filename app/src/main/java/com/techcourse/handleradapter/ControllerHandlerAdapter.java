package com.techcourse.handleradapter;

import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.techcourse.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public Object handle(final HttpServletRequest request, final HttpServletResponse response,
                         final Object handler) throws Exception {
        return ((Controller) handler).execute(request, response);
    }
}
