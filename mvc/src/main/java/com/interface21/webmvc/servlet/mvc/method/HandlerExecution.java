package com.interface21.webmvc.servlet.mvc.method;

import com.interface21.webmvc.servlet.ModelAndView;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        validateHandlerState();

        try {
            final Object result = method.invoke(handler, request, response);
            return convertToModelAndView(result);
        } catch (final InvocationTargetException e) {
            // 실제 메서드에서 발생한 예외 꺼내기
            final Throwable cause = e.getCause();
            if (cause instanceof Exception) {
                throw (Exception) cause;  // 원본 예외 그대로 던지기
            }
            throw new Exception("Handler method threw an error: " + method.getName(), cause);
        } catch (final IllegalAccessException e) {
            throw new Exception("Cannot access handler method: " + method.getName(), e);
        } catch (final IllegalArgumentException e) {
            throw new Exception("Invalid arguments for handler method: " + method.getName(), e);
        } catch (final Exception e) {
            throw new Exception("Unexpected error invoking handler method: " + method.getName(), e);
        }
    }

    private void validateHandlerState() {
        if (handler == null || method == null) {
            throw new IllegalStateException("Handler or method is null");
        }
    }

    private ModelAndView convertToModelAndView(final Object result) {
        if (result instanceof ModelAndView) {
            return (ModelAndView) result;
        }

        throw new IllegalStateException(
                String.format("Handler method %s must return ModelAndView, but returned: %s",
                        method.getName(), result == null ? "null" : result.getClass().getSimpleName()));
    }
}
