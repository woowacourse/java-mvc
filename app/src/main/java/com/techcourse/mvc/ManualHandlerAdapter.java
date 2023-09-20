package com.techcourse.mvc;

import com.techcourse.mvc.exception.CanNotInvokeMethodException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.asis.Controller;
import webmvc.org.springframework.web.servlet.view.JspView;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) {
        try {
            final var controller = (Controller) handler;
            final var viewName = controller.execute(request, response);
            return new ModelAndView(new JspView(viewName));
        } catch (Exception e) {
            e.printStackTrace();
            throw new CanNotInvokeMethodException();
        }
    }

    @Override
    public boolean supports(Object handler) {
        return handler instanceof Controller;
    }
}
