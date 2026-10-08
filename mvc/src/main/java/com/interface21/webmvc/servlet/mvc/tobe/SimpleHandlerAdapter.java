package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.asis.Controller;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class SimpleHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof Controller || handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        if (handler instanceof Controller controller) {
            return new ModelAndView(new JspView(controller.execute(request, response)));
        }
        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }
        throw new IllegalArgumentException("Unsupported handler type: " + handler.getClass().getName());
    }
}
