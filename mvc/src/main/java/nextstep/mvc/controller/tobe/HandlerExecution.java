package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {
    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method method) {
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(declaredObject, request, response);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
