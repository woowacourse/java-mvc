package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private static final String HANDLER_CLASS_NAME = HandlerExecutionHandlerAdapter.class.getCanonicalName();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        validateHandlerType(handler);
        final var handlerExecution = (HandlerExecution) handler;
        
        return handlerExecution.handle(request, response);
    }

    private void validateHandlerType(Object handler) {
        if (!supports(handler)) {
            throw new HandlerAdapterException("unsupported handler adaptor for " + HANDLER_CLASS_NAME);
        }
    }

}
