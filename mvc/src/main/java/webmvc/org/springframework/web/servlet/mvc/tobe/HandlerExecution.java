package webmvc.org.springframework.web.servlet.mvc.tobe;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import webmvc.org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HandlerExecution {

    private final Method method;
    private final Object instance;

    public HandlerExecution(final Method method, final Object instance) {
        this.method = method;
        this.instance = instance;
    }

    public ModelAndView handle(final HttpServletRequest request, final HttpServletResponse response) throws InvocationTargetException, IllegalAccessException {
        return (ModelAndView) method.invoke(instance, request, response);
    }
}
