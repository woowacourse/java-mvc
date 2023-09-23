package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerAdapter;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean support(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request,
                               final HttpServletResponse response,
                               final Object handler) throws Exception {
        if (!support(handler)) {
            throw new IllegalArgumentException("Invalid Handler");
        }

        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        return handlerExecution.handle(request, response);
    }
}
