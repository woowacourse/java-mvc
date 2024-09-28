package com.interface21.webmvc.servlet.mvc.tobe.handlerAdapter;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ManualHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean canHandle(final Object handler) {
        return handler instanceof Controller;
    }

    @Override
    public void handle(final Object handler, final HttpServletRequest request, final HttpServletResponse response) {
        try {
            String viewName = ((Controller) handler).execute(request, response);
            ModelAndView modelAndView = new ModelAndView(new JspView(viewName));
            modelAndView.render(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
