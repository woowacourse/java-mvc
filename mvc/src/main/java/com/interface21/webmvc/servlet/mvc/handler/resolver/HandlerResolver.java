package com.interface21.webmvc.servlet.mvc.handler.resolver;

import com.interface21.webmvc.servlet.mvc.ModelAndView;
import com.interface21.webmvc.servlet.mvc.controller.Controller;
import com.interface21.webmvc.servlet.mvc.handler.mapping.annotation.HandlerExecution;
import com.interface21.webmvc.servlet.mvc.view.View;
import com.interface21.webmvc.servlet.mvc.view.resolver.ViewResolver;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class HandlerResolver {

    private final ViewResolver viewResolver;

    public HandlerResolver(final ViewResolver viewResolver) {
        this.viewResolver = viewResolver;
    }

    public ModelAndView resolve(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        if (handler instanceof Controller controller) {
            final String viewName = controller.execute(request, response);
            final View view = viewResolver.getView(viewName);
            return new ModelAndView(view);
        }

        if (handler instanceof HandlerExecution handlerExecution) {
            return handlerExecution.handle(request, response);
        }

        throw new ServletException(
                "not exists target Handler: %s %s".formatted(request.getMethod(), request.getRequestURI()));
    }
}
