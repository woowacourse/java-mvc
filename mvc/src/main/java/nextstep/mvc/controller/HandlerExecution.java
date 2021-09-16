package nextstep.mvc.controller;

import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object handler;

    public HandlerExecution(Method method, Object handler) {
        this.method = method;
        this.handler = handler;
    }

    public Object handle(Object... parameters)
        throws Exception {
        return method.invoke(handler, parameters);
    }

    public Method getMethod() {
        return method;
    }
}
