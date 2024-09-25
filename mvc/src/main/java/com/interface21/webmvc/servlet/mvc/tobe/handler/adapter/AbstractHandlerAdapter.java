package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import jakarta.servlet.ServletException;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractHandlerAdapter<T> implements HandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(AbstractHandlerAdapter.class);

    private final Class<T> handlerClass;

    public AbstractHandlerAdapter(Class<T> handlerClass) {
        this.handlerClass = handlerClass;
    }

    @Override
    public boolean supports(Object handler) {
        if (handler == null) {
            return false;
        }
        return handler.getClass().isAssignableFrom(handlerClass);
    }

    @Override
    public Class<?> getHandlerType() {
        return handlerClass;
    }

    protected T castHandler(Object handler) throws ServletException {
        try {
            return handlerClass.cast(handler);
        } catch (ClassCastException exception) {
            log.error("Failed to cast handler of type {} to {}.",
                    handler.getClass().getName(),
                    handlerClass.getName());
            throw new ServletException("Unable to process the handler of type.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AbstractHandlerAdapter<?> that = (AbstractHandlerAdapter<?>) o;
        return Objects.equals(handlerClass, that.handlerClass);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(handlerClass);
    }
}
