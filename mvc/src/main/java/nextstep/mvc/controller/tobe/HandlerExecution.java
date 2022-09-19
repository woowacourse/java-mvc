package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> controller;
    private final Method method;

    public HandlerExecution(final Method method) {
        this.controller = method.getDeclaringClass();
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> constructor = controller.getConstructor();
        Object instance = constructor.newInstance();

        return (ModelAndView) method.invoke(instance, request, response);
    }
}
