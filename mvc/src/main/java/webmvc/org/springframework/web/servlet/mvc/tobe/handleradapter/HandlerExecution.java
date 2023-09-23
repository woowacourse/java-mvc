package webmvc.org.springframework.web.servlet.mvc.tobe.handleradapter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object handler;
    private final Method handlerMethod;

    public HandlerExecution(final Object handler, final Method handlerMethod) {
        validateDeclare(handler, handlerMethod);
        validateHandlerMethod(handlerMethod);
        this.handler = handler;
        this.handlerMethod = handlerMethod;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
        throws InvocationTargetException, IllegalAccessException {

        return (ModelAndView) handlerMethod.invoke(handler, request, response);
    }

    private void validateDeclare(final Object handler, final Method handlerMethod) {
        final Class<?> handlerClass = handler.getClass();
        final Class<?> declaringClass = handlerMethod.getDeclaringClass();
        if (!handlerClass.equals(declaringClass)) {
            throw new IllegalArgumentException("클래스에 해당 메서드가 없습니다.");
        }
    }

    private void validateHandlerMethod(final Method handlerMethod) {
        final List<Class<?>> parameterTypes = Arrays.asList(handlerMethod.getParameterTypes());
        if (parameterTypes.contains(HttpServletRequest.class) &&
            parameterTypes.contains(HttpServletResponse.class)) {
            return;
        }
        throw new IllegalArgumentException("메서드 인자가 적절하지 않습니다. parameters : " + parameterTypes);
    }
}
