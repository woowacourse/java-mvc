package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.handler.HandlerAdapter;
import webmvc.org.springframework.web.servlet.view.JspView;

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
        final Object handle = handlerExecution.handle(request, response);

        if (handle instanceof String) {
            return new ModelAndView(new JspView((String) handle));
        }
        if (handle instanceof ModelAndView) {
            return (ModelAndView) handle;
        }

        throw new IllegalArgumentException("Handler Return Type Not Supported");
    }
}
