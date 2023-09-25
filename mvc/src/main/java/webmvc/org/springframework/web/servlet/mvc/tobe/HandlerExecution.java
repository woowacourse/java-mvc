package webmvc.org.springframework.web.servlet.mvc.tobe;

import java.lang.reflect.Method;
import webmvc.org.springframework.web.servlet.ModelAndView;

public class HandlerExecution {

    private final Object controllerInstance;
    private final Method method;

    public HandlerExecution(final Class<?> controllerClass, final Method method) throws Exception {
        this.controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
        this.method = method;
    }

    public ModelAndView handle(final Object[] arguments) throws Exception {
        return (ModelAndView) method.invoke(controllerInstance, arguments);
    }

    public Method getMethod() {
        return method;
    }
}
