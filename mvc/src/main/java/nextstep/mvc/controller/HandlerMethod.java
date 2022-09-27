package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object handler;
    private final Method method;

    public HandlerMethod(final Object handler, final Method method) {
        this.handler = handler;
        this.method = method;
    }

    public Object invoke(Object... parameters) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(handler, parameters);
    }
}
