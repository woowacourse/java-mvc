package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;

public class LegacyHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean isSame(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public String execute(final Object handler, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final Controller controller = (Controller) handler;
        return controller.execute(request, response);
    }
}
