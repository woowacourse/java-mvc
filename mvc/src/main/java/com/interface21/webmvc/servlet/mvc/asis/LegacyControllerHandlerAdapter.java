package com.interface21.webmvc.servlet.mvc.asis;

import com.interface21.webmvc.servlet.HandlerAdapter;
import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LegacyControllerHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        final String viewName = ((Controller) handler).execute(request, response);
        return new ModelAndView(new JspView(viewName));
    }
}
