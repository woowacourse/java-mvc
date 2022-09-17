package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Method method;

    public HandlerExecution(final Method method) {
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response)
            throws InvocationTargetException, InstantiationException, IllegalAccessException {

        final var controller = emptyConstructor(method).newInstance();
        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Constructor<?> emptyConstructor(final Method method) {
        final var declaringClass = method.getDeclaringClass();
        final var constructors = List.of(declaringClass.getConstructors());

        return constructors.stream()
                .filter(this::hasNoParameters)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean hasNoParameters(final Constructor<?> constructor) {
        return constructor.getParameterCount() == 0;
    }
}
