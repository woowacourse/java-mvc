package nextstep.mvc.controller.tobe;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerMethod {

    private final Object executionObject;
    private final Method method;

    public HandlerMethod(final Object executionObject, final Method method) {
        this.executionObject = executionObject;
        this.method = method;
    }

    public Object invoke(Object... parameters) throws InvocationTargetException, IllegalAccessException {
        return method.invoke(executionObject, parameters);
    }
}
