package com.interface21.webmvc.servlet.adapter;

import com.interface21.webmvc.servlet.Handler;
import com.interface21.webmvc.servlet.HandlerType;
import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public abstract class AbstractHandlerAdapter implements HandlerAdapter {

    @Override
    public final ModelAndView handle(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) throws Exception {
        validate(handler);

        ModelAndView modelAndView = null;
        Exception exception = null;
        boolean preHandleCalled = false;

        try {
            preHandle(handler, request, response);
            preHandleCalled = true;

            modelAndView = doHandle(handler, request, response);
            postHandle(modelAndView, request, response);
            return modelAndView;
        } catch (final Exception e) {
            exception = e;
            throw e;
        } finally {
            if (preHandleCalled) {
                afterCompletion(handler, request, response, exception);
            }
        }
    }

    private void validate(final Handler handler) {
        if (handler.type() != supportedType()) {
            throw new IllegalArgumentException(
                    "핸들러 타입이 올바르지 않습니다: 기대 값 = %s, 실제 값 = %s".formatted(supportedType(), handler.type()));
        }
    }

    protected abstract HandlerType supportedType();

    protected abstract ModelAndView doHandle(
            Handler handler,
            HttpServletRequest request,
            HttpServletResponse response
    ) throws Exception;

    protected void preHandle(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
    }

    protected void postHandle(
            final ModelAndView modelAndView,
            final HttpServletRequest request,
            final HttpServletResponse response
    ) {
    }

    protected void afterCompletion(
            final Handler handler,
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Exception exception
    ) {
    }
}
