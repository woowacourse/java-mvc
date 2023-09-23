package com.techcourse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class LegacyHandlerAdaptor implements HandlerAdaptor {

    @Override
    public boolean isSame(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView execute(final Object handler, final HttpServletRequest request,
                                final HttpServletResponse response)
            throws Exception {
        final Controller controller = (Controller) handler;
        final String viewName = controller.execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
