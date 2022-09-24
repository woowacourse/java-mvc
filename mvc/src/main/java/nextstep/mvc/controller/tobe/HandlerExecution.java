package nextstep.mvc.controller.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import nextstep.mvc.view.ModelAndView;

public class HandlerExecution {

    private final Class<?> aClass;
    private final Method method;

    public HandlerExecution(Class<?> aClass, Method method) {
        this.aClass = aClass;
        this.method = method;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        Constructor<?> constructor = aClass.getConstructor();
        return (ModelAndView) method.invoke(constructor.newInstance(), request, response);
    }
}
