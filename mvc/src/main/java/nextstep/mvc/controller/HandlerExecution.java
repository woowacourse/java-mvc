package nextstep.mvc.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Object controller;
    private final Method handler;

    public HandlerExecution(Object controller, Method handler) {
        this.controller = controller;
        this.handler = handler;
    }

    public Object handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return handler.invoke(controller, request, response);
    }
}
