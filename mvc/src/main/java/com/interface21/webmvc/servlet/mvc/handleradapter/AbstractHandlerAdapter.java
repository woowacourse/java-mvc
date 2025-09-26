package com.interface21.webmvc.servlet.mvc.handleradapter;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.ParameterizedType;

public abstract class AbstractHandlerAdapter<T> implements HandlerAdapter {

    private final Class<T> supportedType;

    @SuppressWarnings("unchecked")
    public AbstractHandlerAdapter() {
        this.supportedType = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    @Override
    public final boolean supports(final Object handler) {
        return supportedType.isInstance(handler);
    }

    @Override
    public final ModelAndView handle(
            final Object handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        T typedHandler = supportedType.cast(handler);
        return handleInternal(typedHandler, request, response);
    }

    protected abstract ModelAndView handleInternal(
            final T handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception;
}
