package com.interface21.webmvc.servlet.processor;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerProcessor implements HandlerProcessor {

    @Override
    public boolean supports(Object handler) {
        return Controller.class.isAssignableFrom(handler.getClass());
    }

    @Override
    public ModelAndView process(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
        try {
            final String viewName = ((Controller) handler).execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
