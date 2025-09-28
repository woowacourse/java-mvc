package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import com.interface21.webmvc.servlet.view.JsonView;
import com.interface21.webmvc.servlet.view.JspView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Map;

public class HandlerExecutionAdapter implements HandlerAdapter {

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler) throws Exception {
        final var result = ((HandlerExecution) handler).handle(request, response);

        if (result instanceof Map<?, ?> map) {
            var mav = new ModelAndView(new JsonView());
            map.forEach((k, v) -> mav.addObject((String) k, v));
            return mav;
        }
        if (result instanceof ModelAndView mav) {
            return mav;
        }
        if (result instanceof String viewName) {
            return new ModelAndView(new JspView(viewName));
        }
        throw new IllegalStateException("Unsupported return type: " + result.getClass().getName());
    }
}
