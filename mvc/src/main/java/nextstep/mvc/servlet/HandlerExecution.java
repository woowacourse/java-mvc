package nextstep.mvc.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

import java.lang.reflect.Method;

public class HandlerExecution {
    private final Method method;
    private final Object execution;

    public HandlerExecution(Method method, Object execution) {
        this.method = method;
        this.execution = execution;
    }

    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response) {
        try {
            return (ModelAndView) method.invoke(execution, request, response);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
