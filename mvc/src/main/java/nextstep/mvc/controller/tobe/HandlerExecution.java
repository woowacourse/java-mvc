package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method method;

    public HandlerExecution(Object handler, Method method) {
        this.handler = handler;
        this.method = method;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(handler, request, response);
    }

    public Object getHandler() {
        return handler;
    }
}
