package nextstep.mvc.mapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(Object declaredObject, Method handler) {
        this.declaredObject = declaredObject;
        this.method = handler;
    }

    public boolean hasReturnTypeOf(Class<?> returnType) {
        return method.getReturnType().equals(returnType);
    }

    public Object handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return method.invoke(declaredObject, request, response);
    }
}
