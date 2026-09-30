package com.interface21.webmvc.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.interface21.webmvc.servlet.ModelAndView;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.BiFunction;

public class HandlerExecution {

    private final BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> handleMethod;

    private HandlerExecution(
        BiFunction<HttpServletRequest, HttpServletResponse, ModelAndView> handleMethod) {
        this.handleMethod = handleMethod;
    }

    public static HandlerExecution from(final Object controller, final Method handleMethod) {
        return new HandlerExecution(((request, response) -> {
            try {
                return (ModelAndView) handleMethod.invoke(controller, request, response);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }));
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return handleMethod.apply(request, response);
    }
}
