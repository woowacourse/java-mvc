package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

class HandlerExecution {

    private final Object controller;
    private final Method method;

    HandlerExecution(final Object controller, final Method method) {
        this.controller = controller;
        this.method = method;
    }

    ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            return (ModelAndView)method.invoke(controller, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalArgumentException("method invocation failed");
        }
    }
}
