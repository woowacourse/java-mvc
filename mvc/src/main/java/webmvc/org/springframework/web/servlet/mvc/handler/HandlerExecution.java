package webmvc.org.springframework.web.servlet.mvc.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static java.lang.String.format;

public class HandlerExecution {

    private static final int REQUEST_INDEX = 0;
    private static final int RESPONSE_INDEX = 1;
    private static final int VALID_ARGUMENTS_SIZE = 2;

    private final Object invoker;
    private final Method method;

    public HandlerExecution(final Object invoker, final Method method) {
        validate(method);
        this.invoker = invoker;
        this.method = method;
    }

    private void validate(final Method method) {
        final Parameter[] parameters = method.getParameters();

        if (parameters.length != VALID_ARGUMENTS_SIZE) {
            throw new IllegalStateException(format("인자는 %d개여야 합니다.", VALID_ARGUMENTS_SIZE));
        }
        if (parameters[REQUEST_INDEX].getType() != HttpServletRequest.class) {
            throw new IllegalStateException("HttpServletRequest가 필요합니다.");
        }
        if (parameters[RESPONSE_INDEX].getType() != HttpServletResponse.class) {
            throw new IllegalStateException("HttpServletResponse가 필요합니다.");
        }
        if (method.getReturnType() != ModelAndView.class) {
            throw new IllegalStateException("ModelAndView를 리턴해야 합니다.");
        }
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        return (ModelAndView) method.invoke(invoker, request, response);
    }
}
