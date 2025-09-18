package com.interface21.webmvc.servlet.mvc.method;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandlerExecutionAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HandlerExecutionAdapter.class);

    @Override
    public boolean supports(final Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response,
                               final Object handler)
            throws Exception {
        validateHandler(handler);

        final HandlerExecution handlerExecution = (HandlerExecution) handler;
        log.debug("Executing handler: {}", handlerExecution.getClass().getSimpleName());

        return handlerExecution.handle(request, response);
    }

    private void validateHandler(final Object handler) {
        if (!supports(handler)) {
            throw new IllegalArgumentException(
                    String.format("Handler of type %s is not supported by HandlerExecutionAdapter",
                            handler.getClass().getName()));
        }
    }
}
