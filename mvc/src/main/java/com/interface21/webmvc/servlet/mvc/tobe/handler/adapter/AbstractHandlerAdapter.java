package com.interface21.webmvc.servlet.mvc.tobe.handler.adapter;

import java.util.Objects;

public abstract class AbstractHandlerAdapter<T> implements HandlerAdapter {

    private final Class<T> handlerClass;

    public AbstractHandlerAdapter(Class<T> handlerClass) {
        this.handlerClass = handlerClass;
    }

    @Override
    public boolean supports(Object handler) {
        return handlerClass.isInstance(handler);
    }

    protected T castHandler(Object handler) {
        return handlerClass.cast(handler);
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
