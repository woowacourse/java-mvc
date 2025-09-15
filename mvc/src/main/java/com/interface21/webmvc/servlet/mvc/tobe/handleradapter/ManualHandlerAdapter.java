package com.interface21.webmvc.servlet.mvc.tobe.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    public ModelAndView execute(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {

        final var controller = (Controller) handler;
        return controller.execute(request, response);
    }
}
