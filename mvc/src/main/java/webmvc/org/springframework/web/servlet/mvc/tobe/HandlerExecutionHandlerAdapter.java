package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.mvc.HandlerAdapter;
import webmvc.org.springframework.web.servlet.mvc.exception.HandlerAdapterException;

public class HandlerExecutionHandlerAdapter implements HandlerAdapter {

    private static final String HANDLER_CLASS_NAME = HandlerExecutionHandlerAdapter.class.getCanonicalName();

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        validateHandlerType(handler);

        try {
            final var handlerExecution = (HandlerExecution) handler;
            final var modelAndView = handlerExecution.handle(request, response);
            final var view = modelAndView.getView();

            view.render(modelAndView.getModel(), request, response);
        } catch (Exception exception) {
            throw new HandlerAdapterException("cannot adapt handler " + HANDLER_CLASS_NAME, exception);
        }
    }

    private void validateHandlerType(Object handler) {
        if (!supports(handler)) {
            throw new HandlerAdapterException("unsupported handler adaptor for " + HANDLER_CLASS_NAME);
        }
    }

}
