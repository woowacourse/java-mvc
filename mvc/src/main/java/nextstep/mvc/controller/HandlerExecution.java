package nextstep.mvc.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.stream.Stream;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Object declaredObject;
    private final Method method;

    public HandlerExecution(final Object declaredObject, final Method method) {
        validateObjectHasGivenMethod(declaredObject, method);
        this.declaredObject = declaredObject;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, IllegalAccessException {

        return (ModelAndView) method.invoke(declaredObject, request, response);
    }

    private void validateObjectHasGivenMethod(final Object declaredObject, final Method method) {
        if (doesNotContainGivenMethod(declaredObject, method)) {
            throw new IllegalArgumentException("주어진 객체는 해당 메서드를 지니지 않고 있습니다.");
        }
    }
    
    private boolean doesNotContainGivenMethod(final Object declaredObject, final Method method) {
        final var clazz = declaredObject.getClass();
        final var methods = clazz.getMethods();

        return Stream.of(methods)
                .noneMatch(method::equals);
    }
}
