package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.webmvc.servlet.ModelAndView;
import com.interface21.webmvc.servlet.mvc.HandlerAdapter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnnotationHandlerAdapter implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(AnnotationHandlerAdapter.class);

    @Override
    public boolean supports(Object handler) {
        return handler instanceof HandlerExecution;
    }

    @Override
    public ModelAndView handle(final Object handler,
                               final HttpServletRequest request,
                               final HttpServletResponse response) {
        try {
            final var handlerExecution = (HandlerExecution) handler;
            final var handle = handlerExecution.handle(request, response);
            log.debug("Annotation Handler / method : {} ", handlerExecution.method());
            return handle;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
