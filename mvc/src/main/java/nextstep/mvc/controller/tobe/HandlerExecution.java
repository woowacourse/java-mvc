package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;
import nextstep.mvc.exception.InvalidInvokeMethodException;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object executor;
    private final Method method;

    public HandlerExecution(final Object executor, final Method method) {
        this.executor = Objects.requireNonNull(executor);
        this.method = Objects.requireNonNull(method);
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) {
        try {
            final Object invoke = method.invoke(executor, request, response);
            return (ModelAndView) invoke;
        } catch (final Exception exception) {
            throw new InvalidInvokeMethodException();
        }
    }
}
