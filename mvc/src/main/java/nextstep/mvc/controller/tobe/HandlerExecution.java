package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object executor;
    private final Method method;

    public HandlerExecution(final Object executor, final Method method) {
        this.executor = executor;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object invoke = method.invoke(executor, request, response);
        return (ModelAndView) invoke;
    }
}
