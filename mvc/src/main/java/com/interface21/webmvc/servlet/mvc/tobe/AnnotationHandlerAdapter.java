package com.interface21.webmvc.servlet.mvc.tobe;

import com.interface21.context.stereotype.Controller;

public class AnnotationHandlerAdapter {

    public boolean supports(final Object handler) {
        return handler.getClass().isAnnotationPresent(Controller.class);
    }

    public HandlerExecution handle(final Object handler) {
        return new HandlerExecution(handler, handler.getClass().getMethods()[0]);
    }
}
