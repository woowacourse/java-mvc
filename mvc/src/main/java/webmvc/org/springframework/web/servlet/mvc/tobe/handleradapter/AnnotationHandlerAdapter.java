package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.tobe.exception.InvalidHandlerForHandlerAdapterException;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    @Override
    public boolean isHandleable(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler, final HttpServletRequest request,
                               final HttpServletResponse response) throws Exception {
        if (!isHandleable(handler)) {
            throw new InvalidHandlerForHandlerAdapterException(handler, this);
        }
        return ((HandlerExecution) handler).handle(request, response);
    }
}
