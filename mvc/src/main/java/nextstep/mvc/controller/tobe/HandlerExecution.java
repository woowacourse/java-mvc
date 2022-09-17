package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> clazz;
    private final Method method;

    public HandlerExecution(final Class<?> clazz, final Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> constructor = clazz.getConstructor();
        Object instance = constructor.newInstance();
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
