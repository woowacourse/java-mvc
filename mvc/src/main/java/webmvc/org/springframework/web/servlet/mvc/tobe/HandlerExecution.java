package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;
import webmvc.org.springframework.web.servlet.exception.HandlerExecutionNotInitializeException;

public class HandlerExecution {

    private static final String OBJECT_NOT_INITIALIZE_EXCEPTION_MESSAGE = "객체가 초기화되지 않습니다.";

    private final Method method;
    private final Object object;

    public HandlerExecution(final Method method) {
        this.method = method;
        try {
            this.object = method.getDeclaringClass().getConstructor().newInstance();
        } catch (final Exception e) {
            throw new HandlerExecutionNotInitializeException();
        }
    }

    final ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {
        return (ModelAndView) method.invoke(object, request, response);
    }
}
