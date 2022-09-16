package nextstep.mvc.controller.tobe;

import java.lang.reflect.Constructor;
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

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final Object controller = emptyConstructor(method).newInstance();

        return (ModelAndView) method.invoke(controller, request, response);
    }

    private Constructor<?> emptyConstructor(final Method method) {
        final Class<?> declaringClass = method.getDeclaringClass();
        final List<Constructor<?>> constructors = List.of(declaringClass.getConstructors());

        return constructors.stream()
                .filter(this::hasNoParameters)
                .findAny()
                .orElseThrow(RuntimeException::new);
    }

    private boolean hasNoParameters(final Constructor<?> constructor) {
        return constructor.getParameterCount() == 0;
    }
}
